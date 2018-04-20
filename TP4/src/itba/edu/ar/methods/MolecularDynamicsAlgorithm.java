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
    protected double prevPos;
    protected double prev2Pos;
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

    protected double analyticSolution(double time) {
        return Math.exp(-(gama / (2 * mass)) * time) * Math.cos(Math.pow((k / mass) - (Math.pow(gama, 2) / (4 * Math.pow(mass, 2))), 0.5) * time);
    }

    protected double calculateCuadraticError(double calculatedR, double analyticR) {
        return Math.pow(analyticR - calculatedR, 2);
    }

    protected abstract double calculatePosition();

    protected abstract double calculateVelocity();

    protected abstract String getName();

    protected abstract double calculateAcceleration();

    protected abstract void oscillate() throws IOException;
}
