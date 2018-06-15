package itba.edu.ar.methods;

import itba.edu.ar.models.Particle;

import java.util.List;

public class ForceCalculation {

    private double G;

    public ForceCalculation(double g) {
        this.G = g;
    }

    public void setForces(Particle planet, List<Particle> planets) {

        double totalX = 0;
        double totalY = 0;

        for (Particle p : planets) {
            if(!planet.equals(p)) {
                totalX += getGravityForceFrom(planet, p) * getNormalXVector(planet, p);
                totalY += getGravityForceFrom(planet, p) * getNormalYVector(planet, p);
            }
        }

        planet.setForces(totalX, totalY);
    }

    private double getNormalXVector(Particle particle, Particle p) {
        return (p.getX() - particle.getX()) / getDistance(particle, p);
    }

    private double getNormalYVector(Particle particle, Particle p) {
        return (p.getY() - particle.getY()) / getDistance(particle, p);
    }

    private double getXTangencial(Particle particle, Particle p) {
        return - getNormalYVector(particle,p);
    }

    private double getYTangencial(Particle particle, Particle p) {
        return getNormalXVector(particle,p);
    }

    private double getDistance(Particle p1, Particle p2) {
        return Math.sqrt(Math.pow(p1.getX() - p2.getX(), 2) +
                Math.pow(p1.getY() - p2.getY(), 2));
    }

    private double getGravityForceFrom(Particle particle, Particle p) {
        return G * ((particle.getMass() * p.getMass())/ Math.pow(getDistance(particle, p), 2));
    }

}
