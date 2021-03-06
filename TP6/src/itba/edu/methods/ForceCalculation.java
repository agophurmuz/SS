package itba.edu.methods;


import itba.edu.models.Particle;
import itba.edu.models.Position;

import java.util.HashMap;
import java.util.Set;

public class ForceCalculation {


    private double kn;
    private double gama;
    private double sup;
    private double deltaTime;
    private static final double G = -10;
    private HashMap<String, Double> prevSuperpositions;
    private double accelerationTime;
    private double A;
    private double B;
    private double desiredV = 6.0;

    public ForceCalculation(double kn, double gama, double deltaTime, double accelerationTime, double A, double B) {
        this.kn = kn;
        this.gama = gama;
        this.sup = 0;
        this.deltaTime = deltaTime;
        prevSuperpositions = new HashMap<>();
        this.accelerationTime = accelerationTime;
        this.A = A;
        this.B = B;
    }

    private String getSuperpositionKey(Particle p1, Particle p2) {
        String invertedKey = p2.getId() + "-" + p1.getId();

        if (prevSuperpositions.containsKey(invertedKey)) {
            return invertedKey;
        }
        return p1.getId() + "-" + p2.getId();
    }

    private void addSuperposition(Particle p1, Particle p2, double sup) {
        String key = getSuperpositionKey(p1, p2);
        this.prevSuperpositions.put(key, sup);
    }

    private double getPrevSup(Particle p1, Particle p2) {
        String key = getSuperpositionKey(p1, p2);
        Double sup = prevSuperpositions.get(key);

        return sup == null ? 0 : sup;
    }

    public void setForces(Particle particle, Set<Particle> neighbors, Position<Double> target) {

        try {

            double totalX = getDrivingForceX(particle, target);
            double totalY = getDrivingForceY(particle, target);
            for (Particle p : neighbors) {
                if (particle.getId() != p.getId()) {
                    sup = superposition(particle, p);
                    totalX += getNornalForceReceivedFrom(particle, p) * getNormalXVector(particle, p);
                    totalY += getNornalForceReceivedFrom(particle, p) * getNormalYVector(particle, p);
                    addSuperposition(particle, p, sup);
                    if(!p.isWall()) {
                        totalX += getSocialForce(particle, p) * getNormalXVector(particle, p);
                        totalY += getSocialForce(particle, p) * getNormalYVector(particle, p);
                    }
                }
            }

            particle.setForces(totalX, totalY);
        } catch (NullPointerException npe) {
            npe.printStackTrace();
        }
    }

    private double getSocialForce(Particle particle, Particle p) {
        // ya contemplado sup en getNornalForceReceivedFrom
        double auxS = superposition(particle, p);
        return -1*(A * Math.exp((auxS) / B));
    }

    private double getDrivingForceY(Particle particle, Position<Double> target) {
        //System.out.println("desiredV: " + particle.getDesiredV() + " eTy: " + getEtargetY(particle, target) + " prod: " + particle.getDesiredV() * getEtargetY(particle, target) + " vy: " + particle.getVy());
        return particle.getMass() * (((particle.getDesiredV() * getEtargetY(particle, target)) - particle.getVy())/accelerationTime);
    }

    private double getDrivingForceX(Particle particle, Position<Double> target) {
        //System.out.println("desiredV: " + particle.getDesiredV() + " eTx: " + getEtargetX(particle, target) + " prod: " + particle.getDesiredV() * getEtargetX(particle, target) + " vx: " + particle.getVx());
        return particle.getMass() * (((particle.getDesiredV() * getEtargetX(particle, target)) - particle.getVx())/accelerationTime);
    }

    private double getEtargetY(Particle particle, Position<Double> target) {
        return (target.getY() - particle.getY())/(getDistance(target, particle.getPosition()));
    }

    private double getEtargetX(Particle particle, Position<Double> target) {
        return (target.getX() - particle.getX())/(getDistance(target, particle.getPosition()));
    }



    private double getNormalXVector(Particle particle, Particle p) {
        return (p.getX() - particle.getX()) / getDistance(particle.getPosition(), p.getPosition());
    }

    private double getNormalYVector(Particle particle, Particle p) {
        return (p.getY() - particle.getY()) / getDistance(particle.getPosition(), p.getPosition());
    }

    private double getNornalForceReceivedFrom(Particle particle, Particle p) {
        if (sup < 0) {
            sup = 0;
            return 0;
        }
        double prev = getPrevSup(particle, p);
        return (-kn * sup) - (gama * ((sup - prev) / deltaTime));
    }

    private double getDistance(Position<Double> p1, Position<Double> p2) {
        return Math.sqrt(Math.pow(p1.getX() - p2.getX(), 2) +
                Math.pow(p1.getY() - p2.getY(), 2));
    }

    private double superposition(Particle p1, Particle p2) {
        return p1.getRadius() + p2.getRadius() - getDistance(p1.getPosition(), p2.getPosition());
    }
}
