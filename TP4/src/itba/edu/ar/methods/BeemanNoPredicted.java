package itba.edu.ar.methods;

import itba.edu.ar.FileGenerator;

import java.io.FileOutputStream;
import java.io.IOException;

public class BeemanNoPredicted extends MolecularDynamicsAlgorithm {


    private double currA;

    public BeemanNoPredicted(double k, double gama, double r, double v, double mass, double totalTime, double deltaTime) {
        super(k, gama, r, v, mass, totalTime, deltaTime);
        this.currA = 0;
    }

    @Override
    protected double calculatePosition() {
        return r + v * deltaTime + (2 * currA * Math.pow(deltaTime, 2)) / 3 - (prevAcc * Math.pow(deltaTime, 2)) / 6;
    }

    @Override
    protected double calculateVelocity() {
        return v + ((a * deltaTime) / 3 + (5 * currA * deltaTime) / 6 - (prevAcc * deltaTime) / 6);
    }

    @Override
    protected String getName() {
        return "BeemanNoPredicted";
    }

    @Override
    protected double calculateAcceleration() {
        return (-k * r) / mass;
    }

    @Override
    public void oscillate() throws IOException {

        double time = 0;
        int i = 0;
        double cuadraticErrorStep = 0;
        double analyticR;
        FileOutputStream fileOutputStream = FileGenerator.createFile(getName() + ".csv");
        FileGenerator.addTitle(fileOutputStream);
        while(time <= totalTime) {
            analyticR = analyticSolution(time);
            cuadraticErrorStep += calculateCuadraticError(r, analyticR);
            FileGenerator.addLine(r, analyticR, time, fileOutputStream);
            //currA = calculateAcceleration();
            r = calculatePosition();
            a = calculateAcceleration();
            v = calculateVelocity();
            prevAcc = currA;
            currA = a;
            time+=deltaTime;
            i++;
        }

        cuadraticError = cuadraticErrorStep / i;
        System.out.println("BeemanNoPredicted error: " + cuadraticError);
        FileGenerator.addCuadraticError(cuadraticError, fileOutputStream);
        fileOutputStream.close();
    }
}
