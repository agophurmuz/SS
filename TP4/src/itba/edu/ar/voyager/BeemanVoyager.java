package itba.edu.ar.voyager;

import itba.edu.ar.FileGenerator;
import itba.edu.ar.models.Position;

import java.io.FileOutputStream;
import java.util.List;

public class BeemanVoyager {

    private double ax;
    private double ay;
    private double prevAccX;
    private double prevAccY;
    private double currAx;
    private double currAy;
    private double totalTime;
    private double deltaTime;
    private Body body;
    private List<List<Body>> bodiesArray;
    private double minDist;
    private int iterNum;

    private static final double VOYAGER_DISTANCE = 1500000; /* 1500 km */
    private static final double VOYAGER_SPEED = 11000; /* 11 km/s */
    private static final double EARTH_RADIUS = 6371000;

    public BeemanVoyager(double totalTime, double deltaTime, Body body, List<List<Body>> bodiesArray) {
        this.totalTime = totalTime;
        this.deltaTime = deltaTime;
        this.prevAccX = 0;
        this.prevAccY = 0;
        this.currAx = 0;
        this.currAy = 0;
        this.bodiesArray = bodiesArray;
        this.body = body;
        this.body.setPosition(voyagerInitialPosition());
        this.body.setvX(voyagerInitialVx());
        this.body.setvY(voyagerInitialVy());
        this.ax = calculateAccelerationX(bodiesArray.get(0));
        this.ay = calculateAccelerationY(bodiesArray.get(0));
        minDist = Double.MAX_VALUE;

    }

    private Position voyagerInitialPosition() {
        Body earth = bodiesArray.get(0).get(1);
        double earthSunAngle = calculateEarthSunAngle(earth);
        double voyagerX = earth.getPosition().getX() + (VOYAGER_DISTANCE + EARTH_RADIUS) *Math.cos(earthSunAngle);
        double voyagerY = earth.getPosition().getY() + (VOYAGER_DISTANCE + EARTH_RADIUS) *Math.sin(earthSunAngle);
        return new Position(voyagerX, voyagerY);
    }

    private double voyagerInitialVx(){
        Body earth = bodiesArray.get(0).get(1);
        return earth.getvX() + VOYAGER_SPEED*Math.cos(calculateVelocityAngle(earth));
    }

    private double voyagerInitialVy(){
        Body earth = bodiesArray.get(0).get(1);
        return earth.getvY() + VOYAGER_SPEED*Math.sin(calculateVelocityAngle(earth));
    }

    private double calculateEarthSunAngle(Body earth){
        double earthSunAngle;
        if (earth.getPosition().getX() == 0) {
            earthSunAngle = Math.signum(earth.getPosition().getY()) * Math.PI / 2;
        }else{
            earthSunAngle = Math.atan(earth.getPosition().getY()/earth.getPosition().getX());
            if ((earth.getPosition().getX() < 0 && earth.getPosition().getY() > 0) || (earth.getPosition().getX() < 0 && earth.getPosition().getY() < 0)){
                earthSunAngle += Math.PI;
            }
        }
        return earthSunAngle;
    }

    private double calculateVelocityAngle(Body earth){
        double velocityAngle;
        if (earth.getvX() == 0) {
            velocityAngle = Math.signum(earth.getvY()) * Math.PI / 2;
        }else{
            velocityAngle = Math.atan(earth.getvY()/earth.getvY());
            if ((earth.getvX() < 0 && earth.getvY() > 0) || (earth.getvX() < 0 && earth.getvY() < 0)){
                velocityAngle += Math.PI;
            }
        }
        return velocityAngle;
    }

    private double calculatePositionX() {
        return body.getPosition().getX() + body.getvX() * deltaTime + (2 * ax * Math.pow(deltaTime, 2)) / 3 - (prevAccX * Math.pow(deltaTime, 2)) / 6;
    }

    private double calculatePositionY() {
        return body.getPosition().getY() + body.getvY() * deltaTime + (2 * ay * Math.pow(deltaTime, 2)) / 3 - (prevAccY * Math.pow(deltaTime, 2)) / 6;
    }

    private double calculateAccelerationX(List<Body> bodies) {
        return body.getAccelerationX(bodies);
    }

    private double calculateAccelerationY(List<Body> bodies) {
        return body.getAccelerationY(bodies);
    }

    private double calculateVelocityX(){
        return body.getvX() + ((ax * deltaTime) / 3 + (5 * currAx * deltaTime) / 6 - (prevAccX * deltaTime) / 6);
    }

    private double calculateVelocityY(){
        return body.getvY() + ((ax * deltaTime) / 3 + (5 * currAy * deltaTime) / 6 - (prevAccX * deltaTime) / 6);
    }

    public void move () {
        double time = 0;
        double rx;
        double ry;
        double vx;
        double vy;
        int i = 0;
        Body saturn = null;
        Body jupiter = null;
        double aux;
        FileOutputStream fileOutputStream = FileGenerator.createFile("simulationMonths.xyz");
        while (time < totalTime) {
            rx = calculatePositionX();
            ry = calculatePositionY();
            body.getPosition().setX(rx);
            body.getPosition().setY(ry);
            //body.setPosition(new Position(rx, ry));
            currAx = ax;
            currAy = ay;
            ax = calculateAccelerationX(bodiesArray.get(i + 1));
            ay = calculateAccelerationY(bodiesArray.get(i + 1));
            vx = calculateVelocityX();
            vy = calculateVelocityY();
            body.setvX(vx);
            body.setvY(vy);

            prevAccX = currAx;
            prevAccY = currAy;
            time += deltaTime;

            for (Body b: bodiesArray.get(i)) {
                if(b.getType().equals(BodyType.SATURN)){
                    saturn = b;
                }
                if(b.getType().equals(BodyType.JUPITER)){
                    jupiter = b;
                }
            }

            aux = body.getDist(saturn, jupiter);
            if (aux < minDist) {
                minDist = aux;
                iterNum = i;
            }
            FileGenerator.addPlanets(body, bodiesArray.get(i), fileOutputStream);
            i++;
        }

        System.out.println("Min dist: " + minDist + " Mes: " + iterNum);
    }
}

//Error debería darons E-20
