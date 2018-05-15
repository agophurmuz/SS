package methods;

import models.Particle;

import java.util.HashMap;
import java.util.Set;

public class ForceCalculation {


    private double kn;
    private double kt;
    private double gama;
    private double sup;
    private double deltaTime;
    private static final double G = -10;
    private HashMap<String, Double> prevSuperpositions;

    public ForceCalculation(double kn, double gama, double deltaTime) {
        this.kn = kn;
        this.kt = 2*kn;
        this.gama = gama;
        this.sup = 0;
        this.deltaTime = deltaTime;
        prevSuperpositions = new HashMap<>();
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

    public void setForces(Particle particle, Set<Particle> neighbors) {

        try {
            double totalX = 0;
            double totalY = particle.getMass() * G;
            for (Particle p : neighbors) {
                if (particle.getId() != p.getId()) {
                    sup = superposition(particle, p);
                    totalX += getNornalForceReceivedFrom(particle, p) * getNormalXVector(particle, p)
                            + getTanForceReceivedFrom(particle, p) * (-getNormalYVector(particle, p));
                    totalY += getNornalForceReceivedFrom(particle, p) * getNormalYVector(particle, p)
                            + getTanForceReceivedFrom(particle, p) * getNormalXVector(particle, p);
                    addSuperposition(particle, p, sup);
                }
            }
            particle.setFx(totalX);
            particle.setFy(totalY);
        } catch (NullPointerException npe) {
            npe.printStackTrace();
        }
    }

    private double getNormalXVector(Particle particle, Particle p) {
        return (p.getX() - particle.getX()) / getDistance(particle, p);
    }

    private double getNormalYVector(Particle particle, Particle p) {
        return (p.getY() - particle.getY()) / getDistance(particle, p);
    }

    private double getNornalForceReceivedFrom(Particle particle, Particle p) {
        if (sup < 0) {
            sup = 0;
            return 0;
        }
        double prev = getPrevSup(particle, p);
        return (-kn * sup) - (gama * ((sup - prev) / deltaTime));
    }

    private double getTanForceReceivedFrom(Particle particle, Particle p){
        if(sup < 0) {
            sup = 0;
            return 0;
        }
        return -kt * sup * (((particle.getVx() - p.getVx()) * (-getNormalYVector(particle, p)))
                + ((particle.getVy() - p.getVy()) * getNormalXVector(particle, p)));
    }

    private double getDistance(Particle p1, Particle p2) {
        return Math.sqrt(Math.pow(p1.getX() - p2.getX(), 2) +
                Math.pow(p1.getY() - p2.getY(), 2));
    }

    private double superposition(Particle p1, Particle p2) {
        return p1.getRadius() + p2.getRadius() - getDistance(p1, p2);
    }
}
