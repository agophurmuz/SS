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
        //System.out.println("Voyager force " + "fx: " + totalX + " fy: " + totalY);
        planet.setForces(totalX, totalY);
    }

    public double getNormalXVector(Particle particle, Particle p) {
        return (p.getX() - particle.getX()) / getDistance(particle, p);
    }

    public double getNormalYVector(Particle particle, Particle p) {
        return (p.getY() - particle.getY()) / getDistance(particle, p);
    }

    public double getXTangencial(Particle particle, Particle p) {
        return - getNormalYVector(particle,p);
    }

    public double getYTangencial(Particle particle, Particle p) {
        return getNormalXVector(particle,p);
    }

    public double getDistance(Particle p1, Particle p2) {
        return Math.sqrt(Math.pow(p1.getX() - p2.getX(), 2) +
                Math.pow(p1.getY() - p2.getY(), 2));
    }

    private double getGravityForceFrom(Particle particle, Particle p) {
        return G * ((particle.getMass() * p.getMass())/ Math.pow(getDistance(particle, p), 2));
    }

    public double[] calculateForce(Particle planet, List<Particle> planets) {
        double[] forces = {0, 0};
        double totalX = 0;
        double totalY = 0;

        for (Particle p : planets) {
            if(!planet.equals(p)) {
                totalX += getGravityForceFrom(planet, p) * getNormalXVector(planet, p);
                totalY += getGravityForceFrom(planet, p) * getNormalYVector(planet, p);
            }
        }
        forces[0] = totalX;
        forces[1] = totalY;
        return forces;
    }
}
