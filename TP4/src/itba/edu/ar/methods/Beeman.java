package itba.edu.ar.methods;

import itba.edu.ar.FileGenerator;

import java.io.FileOutputStream;
import java.io.IOException;

public class Beeman extends MolecularDynamicsAlgorithm {

    protected double predictedV;

    //fixme
    public Beeman(double k, double gama, double r, double v, double mass, double totalTime, double deltaTime) {
        super(k, gama, r, v, mass, totalTime, deltaTime);
    }

    protected double calculatePosition() {
        return r + v * deltaTime + (2 * a * Math.pow(deltaTime, 2)) / 3 - (prevAcc * Math.pow(deltaTime, 2)) / 6;
    }

    //todo usarla en el oscillate de acá
    private double calculateVelocityPredicted() {
        return v + (3 * a * deltaTime) / 2 - (prevAcc * deltaTime) / 2;
    }

    protected double calculateVelocity() {
        /*double futureA = (-k * r -gama * predictedV) / mass;
        return v + (futureA * deltaTime) / 3 + (5 * a * Math.pow(deltaTime, 2)) / 6 - (prevAcc * deltaTime) / 6;*/
        return 0;
    }


    protected String getName() {
        return "Beeman";
    }

    protected double calculateAcceleration() {
        return (-k * r -gama * v) / mass;
    }

    protected double calculateVelocity(double predictedV) {
        double futureA = (-k * r - gama * predictedV) / mass;
        return v + ((futureA * deltaTime) / 3 + (5 * a * deltaTime) / 6 - (prevAcc * deltaTime) / 6 );
    }

    @Override
    public void oscillate() throws IOException {
        double time = 0;
        int i = 0;
        double cuadraticErrorStep = 0;
        double analyticR;
        double predictedV;
        FileOutputStream fileOutputStream = FileGenerator.createFile(getName() + ".csv");
        FileOutputStream fileOutputStreamAnalytic = FileGenerator.createFile("analytic.csv");
        FileOutputStream fileOutputStreamTime = FileGenerator.createFile("time.csv");
        //FileGenerator.addTitle(fileOutputStream);
        while (time <= totalTime) {
            analyticR = analyticSolution(time);
            cuadraticErrorStep += calculateCuadraticError(r, analyticR);
            //FileGenerator.addLine(r, analyticR, time, fileOutputStream);
            if(i%10 == 0) {
                FileGenerator.add(r, fileOutputStream);
                FileGenerator.add(analyticR, fileOutputStreamAnalytic);
                FileGenerator.add(time, fileOutputStreamTime);
            }
            r = calculatePosition();
            predictedV = calculateVelocityPredicted();
            v = calculateVelocity(predictedV);

            prevAcc = a;
            time += deltaTime;
            a = calculateAcceleration();
            i++;

        }

        cuadraticError = cuadraticErrorStep / i;
        System.out.println("Beeman error: " + cuadraticError);
        //FileGenerator.addCuadraticError(cuadraticError, fileOutputStream);
        fileOutputStream.close();
    }

    //Error debería darons E-20
}

