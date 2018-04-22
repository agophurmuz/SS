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

    private double alpha0 = 3.0/16;
    private double alpha1 = 251.0/360;
    private double alpha2 = 1.0;
    private double alpha3 = 11.0/18;
    private double alpha4 = 1.0/6;
    private double alpha5 = 1.0/60;

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
        System.out.println("Gera error: " + cuadraticError);
        FileGenerator.addCuadraticError(cuadraticError, fileOutputStream);
        fileOutputStream.close();
    }

    private void calculateFirstIntegrationStep() {
        r2 = (-k * r -gama * r1) / mass;
        r3 = (-k * r1 -gama * r2) / mass;
        r4 = (-k * r2 -gama * r3) / mass;
        r5 = (-k * r3 -gama * r4) / mass;
        /*r2 = (-k/mass) * r;
        r3 = (-k/mass) * r1;
        r4 = Math.pow((-k/mass),2) * r;
        r5 = Math.pow((-k/mass),2) * r1;*/
    }

    private void predic() {
        predR = r + r1 * deltaTime + r2 * (Math.pow(deltaTime, 2) / factorial(2.0)) + r3 * (Math.pow(deltaTime, 3) / factorial(3.0)) +
                r4 * (Math.pow(deltaTime, 4) / factorial(4.0)) + r5 * (Math.pow(deltaTime, 5) / factorial(5.0));
        predR1 = r1 + r2 * deltaTime + r3 * (Math.pow(deltaTime, 2) / factorial(2.0)) + r4 * (Math.pow(deltaTime, 3) / factorial(3.0)) +
                r5 * (Math.pow(deltaTime, 4) / factorial(4.0));
        predR2 = r2 + r3 * deltaTime + r4 * (Math.pow(deltaTime, 2) / factorial(2.0)) + r5 * (Math.pow(deltaTime, 3) / factorial(3.0));
        predR3 = r3 + r4 * deltaTime + r5 * (Math.pow(deltaTime, 2) / factorial(2.0));
        predR4 = r4 + r5 * deltaTime;
        predR5 = r5;
    }

    private void evaluate() {
        double deltaAcc = calculateAcceleration() - predR2;
        deltaR2 = (deltaAcc * Math.pow(deltaTime, 2)) / factorial(2.0);
    }

    private void correct() {
        r = predR + alpha0 * deltaR2;
        r1 = predR1 + ((alpha1 * deltaR2)/deltaTime);
        r2 = predR2 + ((alpha2 * deltaR2 * factorial(2.0))/Math.pow(deltaTime,2));
        r3 = predR3 + ((alpha3 * deltaR2 * factorial(3.0))/Math.pow(deltaTime,3));
        r4 = predR4 + ((alpha4 * deltaR2 * factorial(4.0))/Math.pow(deltaTime,4));
        r5 = predR5 + ((alpha5 * deltaR2 * factorial(5.0))/Math.pow(deltaTime,5));
    }

    private double factorial(double n) {
        if (n == 1) {
            return 1;
        }
        return n * factorial(n - 1);
    }
}
