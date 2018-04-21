package itba.edu.ar.methods;

import itba.edu.ar.FileGenerator;

import java.io.FileOutputStream;
import java.io.IOException;

public class GearPredictorCorrector extends MolecularDynamicsAlgorithm {

    private double prev1;
    private double prevR1;
    private double prevR2;
    private double prevR3;
    private double prevR4;
    private double prevR5;

    private double r1;
    private double r2;
    private double r3;
    private double r4;
    private double r5;

    private double predR;
    private double predR1;
    private double predR2;
    private double predR3;
    private double predR4;
    private double predR5;

    private double deltaR2;

    private double alpha0 = 3d/16d;
    private double alpha1 = 251d/360d;
    private double alpha2 = 1d;
    private double alpha3 = 11d/18d;
    private double alpha4 = 1d/6d;
    private double alpha5 = 1d/60d;

    public GearPredictorCorrector(double k, double gama, double r, double v, double mass, double totalTime, double deltaTime) {
        super(k, gama, r, v, mass, totalTime, deltaTime);
        this.r1 = v;
    }

    protected double calculatePosition() {
        return 0;
    }

    protected double calculateVelocity() {
        return 0;
    }

    protected String getName() {
        return "GearPredictorCorrector";
    }

    protected double calculateAcceleration() {
        return (-k * r -gama * r1) / mass;
    }

    @Override
    public void oscillate() throws IOException {

        double time = 0;
        int i = 0;
        double cuadraticErrorStep = 0;
        double analyticR;
        calculateFirstIntegrationStep();
        FileOutputStream fileOutputStream = FileGenerator.createFile(getName() + ".csv");
        FileGenerator.addTitle(fileOutputStream);
        while (time <= totalTime) {
            analyticR = analyticSolution(time);
            cuadraticErrorStep += calculateCuadraticError(r, analyticR);
            FileGenerator.addLine(r, analyticR, time, fileOutputStream);
            predic();

            evaluate();

            correct();
            time += deltaTime;
            i++;
        }
        cuadraticError = cuadraticErrorStep / i;
        FileGenerator.addCuadraticError(cuadraticError, fileOutputStream);
        fileOutputStream.close();
    }

    private void calculateFirstIntegrationStep() {
        double fix = -k/mass;
        r2 = fix * r;
        r3 = fix * r1;
        r4 = Math.pow(fix, 2) * r;
        r5 = Math.pow(fix, 2) * r1;
    }

    private void predic() {
        predR = r + r1 * deltaTime + r2 * (Math.pow(deltaTime, 2) / factorial(2)) + r3 * (Math.pow(deltaTime, 3) / factorial(3)) +
                r4 * (Math.pow(deltaTime, 4) / factorial(4)) + r5 * (Math.pow(deltaTime, 5) / factorial(5));
        predR1 = r1 + r2 * deltaTime + r3 * (Math.pow(deltaTime, 2) / factorial(2)) + r4 * (Math.pow(deltaTime, 3) / factorial(3)) +
                r5 * (Math.pow(deltaTime, 4) / factorial(4));
        predR2 = r2 + r3 * deltaTime + r4 * (Math.pow(deltaTime, 2) / factorial(2)) + r5 * (Math.pow(deltaTime, 3) / factorial(3));
        predR3 = r3 + r4 * deltaTime + r5 * (Math.pow(deltaTime, 2) / factorial(2));
        predR4 = r4 + r5 * deltaTime;
        predR5 = r5;
    }

    private void evaluate() {
        double deltaAcc = calculateAcceleration() - predR2;
        deltaR2 = (deltaAcc * Math.pow(deltaTime, 2)) / factorial(2);
    }

    private void correct() {
        r = predR + alpha0 * deltaR2;
        r1 = predR1 + ((alpha1 * deltaR2)/deltaTime);
        r2 = predR2 + ((alpha2 * deltaR2 * factorial(2))/Math.pow(deltaTime,2d));
        r3 = predR3 + ((alpha3 * deltaR2 * factorial(3))/Math.pow(deltaTime,3d));
        r4 = predR4 + ((alpha4 * deltaR2 * factorial(4))/Math.pow(deltaTime,4d));
        r5 = predR5 + ((alpha5 * deltaR2 * factorial(5))/Math.pow(deltaTime,5d));
    }

    private double factorial(double n) {
        if (n == 1) {
            return 1;
        }
        return n * factorial(n - 1);
    }
}
