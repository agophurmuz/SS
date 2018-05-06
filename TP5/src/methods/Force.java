package methods;

import models.Particle;

import java.util.Set;

public class Force {


    private double k;
    private double gama;

    public Force(double k, double gama) {
        this.k = k;
        this.gama = gama;
    }

    public void setForces(Particle particle, Set<Particle> neighbors) {

        try {
            double totalX = 0;
            double totalY = 0;
            for (Particle p : neighbors) {
                if (particle.getId() != p.getId()) {
                    totalX -= getForceReceivedFrom(particle, p, k, gama) * (particle.getX() - p.getX()) / getDistance(particle, p);
                    totalY -= getForceReceivedFrom(particle, p, k, gama) * (particle.getY() - p.getY()) / getDistance(particle, p);
                }
            }
            particle.setFx(totalX);
            particle.setFy(totalY);
        }catch(NullPointerException npe){
            npe.printStackTrace();
        }
    }

    private double getForceReceivedFrom(Particle particle, Particle p, double k, double gama) {
        return (-k * superposition(particle, p) - (gama * superpositionPrime(particle, p)));
    }

    private double getDistance(Particle p1, Particle p2) {
        return Math.sqrt(Math.pow(p1.getX() - p2.getX(), 2) +
                Math.pow(p1.getY() - p2.getY(), 2));
    }

    private double superposition(Particle p1, Particle p2) {
        return p1.getRadius() + p2.getRadius() - getDistance(p1, p2);
    }

    private double superpositionPrime(Particle p1, Particle p2) {
        return -(Math.sqrt(Math.pow(p1.getVx() - p2.getVx(), 2) + Math.pow(p1.getVy() - p2.getVy(), 2)));
    }




}
