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
        return (r - prev2Pos)/(2* deltaTime);
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
        double a = k * r;
        double b = gama * v;
        return ((-a - b) / mass);
    }

    @Override
    public void oscillate() {

        double time = 0;
        int i = 0;
        double cuadraticErrorStep = 0;
        double analyticR;
        double aux;

        FileOutputStream fileOutputStream = FileGenerator.createFile(getName() + ".csv");
        FileGenerator.addTitle(fileOutputStream);

        prevPos = calculatePositionEuler(); //fixme euler
        aux = r;
        r = calculatePosition();
        time += deltaTime;
        a = calculateAcceleration();
        prev2Pos = prevPos;
        prevPos = aux;
        aux = r;
        r = calculatePosition();
        time += deltaTime;
        i+=2;

        //caculateNextPosition
        while (time <= totalTime) {
            prev2Pos = prevPos;
            prevPos = aux;
            analyticR = analyticSolution(time);
            cuadraticErrorStep += calculateCuadraticError(r, analyticR);
            FileGenerator.addLine(r, analyticR, time, fileOutputStream);

            prevAcc = a;

            System.out.println(a);
            v = calculateVelocity();
            a = calculateAcceleration();

            aux = r;
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

    private double calculatePositionEuler() {
        double prevV = calculatePrevV();
        return r - (deltaTime * prevV) + ((Math.pow(-deltaTime, 2) * a) / 2);
    }

    private double calculatePrevV() {
        return v - (deltaTime * a);
    }


}
