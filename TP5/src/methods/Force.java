package methods;

import models.Particle;

import java.util.Set;

public class Force {


    private double k;
    private double gama;
    private double prevSup;
    private double sup;
    private double deltaTime;
    private static final double G = -10;

    public Force(double k, double gama, double deltaTime) {
        this.k = k;
        this.gama = gama;
        this.prevSup = 0;
        this.sup = 0;
        this.deltaTime = deltaTime;
    }

    public void setForces(Particle particle, Set<Particle> neighbors) {

        try {
            double totalX = 0;
            double totalY = particle.getMass() * G;
            prevSup = 0;
            for (Particle p : neighbors) {
                if (particle.getId() != p.getId()) {
                    totalX += getForceReceivedFrom(particle, p, k, gama) * (p.getX() - particle.getX()) / getDistance(particle, p);
                    totalY += getForceReceivedFrom(particle, p, k, gama) * (p.getY() - particle.getY()) / getDistance(particle, p);
                    prevSup = sup;
                }
            }
            particle.setFx(totalX);
            particle.setFy(totalY);
        } catch (NullPointerException npe) {
            npe.printStackTrace();
        }
    }

    private double getForceReceivedFrom(Particle particle, Particle p, double k, double gama) {
        sup = superposition(particle, p);
        //System.out.println("Particle x: " + particle.getX() +" y: "+ particle.getY() + " sup: " + sup );
        if (sup < 0) {
            return 0;
        }
        return (-k * sup); //- (gama * ((sup - prevSup) / deltaTime));
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
