package itba.edu.ar.methods;

import itba.edu.ar.FileGenerator;

import java.io.FileOutputStream;
import java.io.IOException;

public class Verlet extends  MolecularDynamicsAlgorithm{

    public Verlet(double k, double gama, double r, double v, double mass, double totalTime, double deltaTime) {
        super(k, gama, r, v, mass, totalTime, deltaTime);
    }

    @Override
    protected double calculateVelocity() {
        //r ya tiene la siguiente pq la calculó en el paso anterior en oscillate.
        return (r - prev2Pos)/2* deltaTime;
    }

    @Override
    protected double calculatePosition() {
        return 2*r - prevPos + Math.pow(deltaTime,2) * a;
    }

    @Override
    protected String getName() {
        return "Verlet";
    }

    @Override
    protected double calculateAcceleration() {
        return (-(k * r) - (gama * v)) / mass;
    }

    @Override
    public void oscillate() {

        double time = 0;
        int i = 0;
        double cuadraticErrorStep = 0;
        double analyticR;

        FileOutputStream fileOutputStream = FileGenerator.createFile(getName() + ".csv");
        FileGenerator.addTitle(fileOutputStream);

        prevPos = r; //fixme euler
        r = calculatePosition();
        time += deltaTime;
        a = calculateAcceleration();
        prev2Pos = prevPos;
        prevPos = r;
        r = calculatePosition();
        time += deltaTime;
        i+=2;

        //caculateNextPosition
        while (time <= totalTime) {

            analyticR = analyticSolution(time);
            cuadraticErrorStep += calculateCuadraticError(r, analyticR);
            FileGenerator.addLine(r, analyticR, time, fileOutputStream);

            prevAcc = a;

            v = calculateVelocity();
            a = calculateAcceleration();

            prev2Pos = prevPos;
            prevPos = r;
            r = calculatePosition();

            time += deltaTime;

            i++;

        }

        cuadraticError = cuadraticErrorStep / i;
        FileGenerator.addCuadraticError(cuadraticError, fileOutputStream);
        try {
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
