package itba.edu.ar.simulation.methods;

import itba.edu.ar.simulation.models.*;

import java.util.ArrayList;
import java.util.List;

public class BrownianMovement {

    private double L;
    private List<Particle> particles;
    private List<PotentialCrash> crashTimes;
    private PotentialCrash minCrash;

    public BrownianMovement(double l, List<Particle> particles) {
        L = l;
        this.particles = particles;
        this.crashTimes = new ArrayList<>();
        resetMinCrash();
    }

    private void resetMinCrash() {
        this.minCrash = new ParticlePotentialCrash(Double.MAX_VALUE, null, null);
    }

    public double getL() {
        return L;
    }

    public void setL(double l) {
        L = l;
    }

    public List<Particle> getParticles() {
        return particles;
    }

    public void setParticles(List<Particle> particles) {
        this.particles = particles;
    }

    public List<PotentialCrash> getCrashTimes() {
        return crashTimes;
    }

    public void setCrashTimes(List<PotentialCrash> crashTimes) {
        this.crashTimes = crashTimes;
    }

    public PotentialCrash getMinCrash() {
        return minCrash;
    }

    public void setMinCrash(PotentialCrash minCrash) {
        this.minCrash = minCrash;
    }

    public void timeCrashParticleToWall(Particle particle) {

        // calculate times with walls
        double auxX;
        double auxY;
        if (particle.getVx() > 0) {
            auxX = (L - particle.getRadius() - particle.getX()) / particle.getVx();
        } else if (particle.getVx() < 0) {
            auxX = (particle.getRadius() - particle.getX()) / particle.getVx();
        } else {
            auxX = Double.MAX_VALUE;
        }
        if (particle.getVy() > 0) {
            auxY = (L - particle.getRadius() - particle.getY()) / particle.getVy();
        } else if (particle.getVy() < 0) {
            auxY = (0 + particle.getRadius() - particle.getY()) / particle.getVy();
        } else {
            auxY = Double.MAX_VALUE;
        }
        double aux = Math.min(auxX, auxY);
        if (aux < minCrash.getTime()) {
            minCrash = new WallPotentialCrash(aux, particle, auxX < auxY ? Wall.VERTICAL : Wall.HORIZONTAL);
        }
        crashTimes.add(new WallPotentialCrash(auxX, particle, Wall.VERTICAL));
        crashTimes.add(new WallPotentialCrash(auxY, particle, Wall.HORIZONTAL));
    }


    public void timeCrashParticleToParticle(Particle particle1, Particle particle2) {

        //calculate times with particles
        double aux;
        double sigma = particle2.getRadius() + particle1.getRadius();
        double deltaX = particle2.getX() - particle1.getX();
        double deltaY = particle2.getY() - particle1.getY();
        double deltaVx = particle2.getVx() - particle1.getVx();
        double deltaVy = particle2.getVy() - particle1.getVy();
        double vr = deltaVx * deltaX + deltaVy * deltaY;

        double vv = Math.pow(deltaVx, 2) + Math.pow(deltaVy, 2);
        double rr = Math.pow(deltaX, 2) + Math.pow(deltaY, 2);
        double d = Math.pow(vr, 2) - vv * (rr - Math.pow(sigma, 2));
        if (vr >= 0) {
            aux = Double.MAX_VALUE;
        } else if (d < 0) {
            aux = Double.MAX_VALUE;
        } else {
            aux = -(vr + Math.sqrt(d)) / vv;
        }
        if (aux < minCrash.getTime()) {
            minCrash = new ParticlePotentialCrash(aux, particle1, particle2);
        }
        crashTimes.add(new ParticlePotentialCrash(aux, particle1, particle2));
    }

    public void move() {
        for (Particle particle : particles) {
            double x = particle.getX() + particle.getVx() * minCrash.getTime();
            double y = particle.getY() + particle.getVy() * minCrash.getTime();
            particle.setX(x);
            particle.setY(y);
        }
    }

    public void crash() {
        minCrash.crash();
        resetMinCrash();
    }
}
