package itba.edu.ar.methods;

import itba.edu.ar.models.*;

import java.util.ArrayList;
import java.util.List;

public class BrownianMovement {

    private double L;
    private List<Particle> particles;
    private List<PotentialCrash> crashTimes;
    private double minCrashTime;

    public BrownianMovement(double l, List<Particle> particles) {
        L = l;
        this.particles = particles;
        this.crashTimes = new ArrayList<>();
        this.minCrashTime = Double.MAX_VALUE;
    }

    public List<PotentialCrash> timeCrashParticleToWall(List<PotentialCrash> crashTimes, Particle particle) {

        // calculate times with walls
        double auxX = 0;
        double auxY = 0;
        if (particle.getVx() > 0) {
            auxX = (L - particle.getRadius() - particle.getX()) / particle.getVx();
        }
        if (particle.getVx() < 0) {
            auxX = (particle.getRadius() - particle.getX()) / particle.getVx();
        }
        if (particle.getVy() > 0) {
            auxY = (L - particle.getRadius() - particle.getY()) / particle.getVy();
        }
        if (particle.getVy() < 0) {
            auxY = (particle.getRadius() - particle.getY()) / particle.getVy();
        }
        crashTimes.add(new WallPotentialCrash(auxX, particle, null));
        crashTimes.add(new WallPotentialCrash(auxY, particle, null));
        return crashTimes;
    }


    public List<PotentialCrash> timeCrashParticleToParticle(List<PotentialCrash> crashTimes, Particle particle1, Particle particle2) {

        //calculate times with particles
        double aux = 0;
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
            aux = -(vr + Math.sqrt(d) / vv);
        }
        crashTimes.add(new ParticlePotentialCrash(aux, particle1, particle2));
        return crashTimes;
    }
}
