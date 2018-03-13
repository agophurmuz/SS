package itba.edu.ar;

import itba.edu.ar.methods.BoundaryCondition;
import itba.edu.ar.methods.CellIndexMethod;
import itba.edu.ar.models.Particle;
import itba.edu.ar.models.Position;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

    public Set<Particle> move(Board board) {
        Set<Particle> newParticles = new HashSet<>();
        CellIndexMethod cellIndexMethod = new CellIndexMethod(BoundaryCondition.NON_PERIODIC, M, r, board);
        Map<Particle, Set<Particle>> particlesNeighbors = cellIndexMethod.calculateDistance();
        Set<Particle> particles = particlesNeighbors.keySet();
        for (Particle particle : particles) {
            double xCoord = particle.getPosition().getX().doubleValue() + particle.getSpeed() * Math.cos(particle.getAngle());
            double yCoord = particle.getPosition().getY().doubleValue() + particle.getSpeed() * Math.sin(particle.getAngle());
            double sinSum = Math.sin(particle.getAngle());
            double cosSum = Math.cos(particle.getAngle());
            for (Particle neighbor : particlesNeighbors.get(particle)) {
                sinSum += Math.sin(neighbor.getAngle());
                cosSum += Math.cos(neighbor.getAngle());
            }
            int cant = particlesNeighbors.get(particle).size();
            double averageAngle = Math.atan2((sinSum / cant), (cosSum / cant)) + (Math.random() * eta - (eta / 2));
            newParticles.add(new Particle(new Position(xCoord, yCoord), particle.getId(), particle.getRadius(), averageAngle, particle.getSpeed()));
        }
        for (int x = 0; x < M; x++) {
            for (int y = 0; y < M; y++) {
            List<Particle> list = board.getBoard()[x][y].getPatricleList();
                for (Particle p1 : list) {
                    for (Particle p2 : newParticles) {
                        if(p1.getId() == p2.getId()){
                            p1.setPosition(p2.getPosition());
                            p1.setAngle(p2.getAngle());
                        }
                    }
                }
            }
        }
        return newParticles;
    }

    public double polarization(Set<Particle> particles){
        double vxSum = 0;
        double vySum = 0;
        for (Particle particle : particles) {
            vxSum += particle.getSpeed() * Math.cos(particle.getAngle());
            vySum += particle.getSpeed() * Math.sin(particle.getAngle());
        }
        return Math.sqrt(Math.pow(vxSum, 2) + Math.pow(vySum, 2)) / (speed * particles.size());
    }
}
