package itba.edu.ar.methods;

import itba.edu.ar.FileGenerator;

import java.io.FileOutputStream;
import java.io.IOException;

public abstract class MolecularDynamicsAlgorithm {

    protected double k;
    protected double gama;
    protected double r;
    protected double v;
    protected double mass;
    protected double a;
    protected double prevAcc;
    protected double cuadraticError;
    protected double totalTime;
    protected double deltaTime;


    public MolecularDynamicsAlgorithm(double k, double gama, double r, double v, double mass, double totalTime, double deltaTime) {
        this.k = k;
        this.gama = gama;
        this.r = r;
        this.v = v;
        this.mass = mass;
        this.totalTime = totalTime;
        this.deltaTime = deltaTime;
        this.prevAcc = 0;
        this.a = calculateAcceleration();
    }

    private double calculateAcceleration() {
        return (-k * r -gama * v) / mass;
    }

    private double analyticSolution(double time) {
        return Math.exp(-(gama / (2 * mass)) * time) * Math.cos(Math.pow((k / mass) - (Math.pow(gama, 2) / (4 * Math.pow(mass, 2))), 0.5) * time);
    }

    private double calculateCuadraticError(double calculatedR, double analyticR) {
        return Math.pow(analyticR - calculatedR, 2);
    }

    public void oscillate() throws IOException {

        double time = 0;
        int i = 0;
        double cuadraticErrorStep = 0;
        double analyticR;
        FileOutputStream fileOutputStream = FileGenerator.createFile(getName() + ".csv");
        FileGenerator.addTitle(fileOutputStream);
        while (time <= totalTime) {

            analyticR = analyticSolution(time);
            cuadraticErrorStep += calculateCuadraticError(r, analyticR);
            FileGenerator.addLine(r, analyticR, time, fileOutputStream);

            r = calculatePosition();
            v = calculateVelocity();

            prevAcc = a;
            time += deltaTime;
            a = calculateAcceleration();
            i++;

        }

        cuadraticError = cuadraticErrorStep / i;
        FileGenerator.addCuadraticError(cuadraticError, fileOutputStream);
        fileOutputStream.close();

    }

    protected abstract double calculateVelocity();

    protected abstract double calculatePosition();

    protected abstract String getName();
}
