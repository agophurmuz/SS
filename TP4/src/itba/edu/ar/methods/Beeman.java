package itba.edu.ar.methods;

public class Beeman extends MolecularDynamicsAlgorithm {


    public Beeman(double k, double gama, double r, double v, double mass, double totalTime, double deltaTime) {
        super(k, gama, r, v, mass, totalTime, deltaTime);
    }

    protected double calculatePosition() {
        return r + v * deltaTime + (2 * a * Math.pow(deltaTime, 2)) / 3 - (prevAcc * Math.pow(deltaTime, 2)) / 6;
    }

    protected double calculateVelocityPredicted() {
        return v + (3 * a * deltaTime) / 2 - (prevAcc * deltaTime) / 2;
    }

    protected double calculateVelocity(double predictedV) {
        double futureA = (-k * r -gama * predictedV) / mass;
        return v + (futureA * deltaTime) / 3 + (5 * a * Math.pow(deltaTime, 2)) / 6 - (prevAcc * deltaTime) / 6;
    }


    protected String getName() {
        return "Beeman";
    }

    //Error debería darons E-20
}

