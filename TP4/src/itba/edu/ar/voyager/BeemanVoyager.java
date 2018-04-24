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
    private Body voyager;
    private List<Body> planets;
    private double minDist;
    private int iterNum;

    private static final double VOYAGER_DISTANCE = 1500000; /* 1500 km */
    private static final double VOYAGER_SPEED = 11000; /* 11 km/s */
    private static final double EARTH_RADIUS = 6371000;

    public BeemanVoyager(double totalTime, double deltaTime, Body voyager, List<Body> planets) {
        this.totalTime = totalTime;
        this.deltaTime = deltaTime;
        this.prevAccX = 0;
        this.prevAccY = 0;
        this.currAx = 0;
        this.currAy = 0;
        this.planets = planets;
        this.voyager = voyager;
        this.voyager.setPosition(voyagerInitialPosition());
        this.voyager.setvX(voyagerInitialVx());
        this.voyager.setvY(voyagerInitialVy());
        minDist = Double.MAX_VALUE;

    }

    private Position voyagerInitialPosition() {
        Body earth = planets.get(1);
        double earthSunAngle = calculateEarthSunAngle(earth);
        double voyagerX = earth.getPosition().getX() + (VOYAGER_DISTANCE + EARTH_RADIUS) *Math.cos(earthSunAngle);
        double voyagerY = earth.getPosition().getY() + (VOYAGER_DISTANCE + EARTH_RADIUS) *Math.sin(earthSunAngle);
        return new Position(voyagerX, voyagerY);
    }

    private double voyagerInitialVx(){
        Body earth = planets.get(1);
        return earth.getvX() + VOYAGER_SPEED*Math.cos(calculateVelocityAngle(earth));
    }

    private double voyagerInitialVy(){
        Body earth = planets.get(1);
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

    private double calculatePositionX(Body body) {
        return body.getPosition().getX() + body.getvX() * deltaTime + (2 * currAx * Math.pow(deltaTime, 2)) / 3 - (prevAccX * Math.pow(deltaTime, 2)) / 6;
    }

    private double calculatePositionY(Body body) {
        return body.getPosition().getY() + body.getvY() * deltaTime + (2 * currAy * Math.pow(deltaTime, 2)) / 3 - (prevAccY * Math.pow(deltaTime, 2)) / 6;
    }

    private double calculateAccelerationX(Body body) {
        return body.getAccelerationX(planets);
    }

    private double calculateAccelerationY(Body body) {
        return body.getAccelerationY(planets);
    }

    private double calculateVelocityX(Body body){
        return body.getvX() + ((ax * deltaTime) / 3 + (5 * currAx * deltaTime) / 6 - (prevAccX * deltaTime) / 6);
    }

    private double calculateVelocityY(Body body){
        return body.getvY() + ((ay * deltaTime) / 3 + (5 * currAy * deltaTime) / 6 - (prevAccY * deltaTime) / 6);
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
        for (Body planet:planets) {
            if (planet.getType() != BodyType.SUN){
                double auxAx = calculateAccelerationX(planet);
                double auxAy = calculateAccelerationY(planet);
                planet.setvX(planet.getvX() + deltaTime * auxAx);
                planet.setvY(planet.getvY() + deltaTime * auxAy);
                double x = planet.getPosition().getX() + deltaTime * planet.getvX() + Math.pow(deltaTime, 2) * auxAx;
                double y = planet.getPosition().getY() + deltaTime * planet.getvY() + Math.pow(deltaTime, 2) * auxAy;
                planet.setPosition(new Position(x, y));
            }
        }

        FileOutputStream fileOutputStream = FileGenerator.createFile("simulationMonths.xyz");
        while (time <= totalTime) {
            for (Body p: planets) {
                if (p.getType() != BodyType.SUN){
                    currAx = calculateAccelerationX(p);
                    currAy = calculateAccelerationY(p);

                    rx = calculatePositionX(p);
                    ry = calculatePositionY(p);
                    p.setPosition(new Position(rx, ry));

                    ax = calculateAccelerationX(p);
                    ay = calculateAccelerationY(p);

                    vx = calculateVelocityX(p);
                    vy = calculateVelocityY(p);
                    p.setvY(vx);
                    p.setvY(vy);
                    prevAccX = currAx;
                    prevAccY = currAy;
                    time += deltaTime;
                }
            }




            /*for (Body b: bodiesArray.get(i)) {
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
            */
            FileGenerator.addPlanets(voyager, planets, fileOutputStream);
            //i++;
        }

        //System.out.println("Min dist: " + minDist + " Mes: " + iterNum);
    }
}

//Error debería darons E-20
