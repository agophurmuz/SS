package itba.edu.ar;

import itba.edu.ar.methods.BoundaryCondition;
import itba.edu.ar.methods.CellIndexMethod;
import itba.edu.ar.models.Particle;
import itba.edu.ar.models.Position;

import java.util.*;

public class SelfMovingParticles {

    private int L;
    private double r;
    private double eta;
    private int M;
    private double speed;

    public SelfMovingParticles(int l, double r, double eta, int M, double speed) {
        L = l;
        this.r = r;
        this.eta = eta;
        this.M = M;
        this.speed = speed;
    }

    public List<Particle> move(List<Particle> particles) {
        List<Particle> newParticles = new ArrayList<>();
        CellIndexMethod cellIndexMethod = new CellIndexMethod(BoundaryCondition.NON_PERIODIC, M, L, r, particles);
        Map<Particle, Set<Particle>> particlesNeighbors = cellIndexMethod.getParticleNeighbors();
        for (Particle particle : particles) {
            double xCoord = particle.getPosition().getX().doubleValue() + particle.getSpeed() * Math.cos(particle.getAngle());
            double yCoord = particle.getPosition().getY().doubleValue() + particle.getSpeed() * Math.sin(particle.getAngle());
            if(xCoord >= L) {
                xCoord -= L;
            } else if (xCoord < 0) {
                xCoord += L;
            }
            if (yCoord >= L) {
                yCoord -= L;
            } else if (yCoord < 0) {
                yCoord += L;
            }
            double sinSum = Math.sin(particle.getAngle());
            double cosSum = Math.cos(particle.getAngle());
            int cant;
            if(particlesNeighbors.containsKey(particle)){
                for (Particle neighbor : particlesNeighbors.get(particle)) {
                    sinSum += Math.sin(neighbor.getAngle());
                    cosSum += Math.cos(neighbor.getAngle());
                }
                cant = particlesNeighbors.get(particle).size();
            } else {
                cant = 1;
            }
            double averageAngle = Math.atan2((sinSum / cant), (cosSum / cant)) + (Math.random() * eta - (eta / 2));
            newParticles.add(new Particle(new Position(xCoord, yCoord), particle.getId(), particle.getRadius(), averageAngle, particle.getSpeed()));
        }
        return newParticles;
    }

    public double polarization(List<Particle> particles){
        double vxSum = 0;
        double vySum = 0;
        for (Particle particle : particles) {
            vxSum += particle.getSpeed() * Math.cos(particle.getAngle());
            vySum += particle.getSpeed() * Math.sin(particle.getAngle());
        }
        return Math.sqrt(Math.pow(vxSum, 2) + Math.pow(vySum, 2)) / (speed * particles.size());
    }
}
