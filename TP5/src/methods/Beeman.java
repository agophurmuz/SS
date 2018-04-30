package methods;

import models.Particle;
import models.Position;

import java.util.Set;

public class Beeman {

    private double totalTime;
    private double deltaTime;
    private Force force;

    public Beeman(Force force, double totalTime, double deltaTime) {
        this.totalTime = totalTime;
        this.deltaTime = deltaTime;
        this.force = force;
    }

    private double calculatePositionX(Particle particle) {
        return particle.getX() + particle.getVx() * deltaTime + (2 * calculateAccelerationX(particle) * Math.pow(deltaTime, 2)) / 3
                - (particle.getPrevAccX() * Math.pow(deltaTime, 2)) / 6;
    }

    private double calculatePositionY(Particle particle) {
        return particle.getY() + particle.getVy() * deltaTime + (2 * calculateAccelerationY(particle) * Math.pow(deltaTime, 2)) / 3
                - (particle.getPrevAccY() * Math.pow(deltaTime, 2)) / 6;
    }


    protected String getName() {
        return "Beeman";
    }

    private double calculateAccelerationX(Particle particle) {
        return particle.getFx() / particle.getMass();
    }

    private double calculateAccelerationY(Particle particle) {
        return particle.getFy() / particle.getMass();
    }


    private double calculateVelocityX(Particle particle) {
        return particle.getVx() + ((calculateAccelerationX(particle) * deltaTime) / 3
                + (5 * calculateAccelerationX(particle) * deltaTime) / 6 - (particle.getPrevAccX() * deltaTime) / 6 );
    }

    private double calculateVelocityY(Particle particle) {
        return particle.getVy() + ((calculateAccelerationY(particle) * deltaTime) / 3
                + (5 * calculateAccelerationY(particle) * deltaTime) / 6 - (particle.getPrevAccY() * deltaTime) / 6 );
    }

    public Particle moveParticle(Particle particle, Set<Particle> neighbors) {

        force.setForces(particle, neighbors);

        double x = calculatePositionX(particle);
        double y = calculatePositionY(particle);
        Position newPosition = new Position(x, y);

        double vx = calculateVelocityX(particle);
        double vy = calculateVelocityY(particle);

        double prevAccX = particle.getFx() / particle.getMass();
        double prevAccY = particle.getFy() / particle.getMass();

        return new Particle(particle.getId(), newPosition, vx, vy, particle.getRadius(), particle.getMass(), prevAccX, prevAccY);

    }

}
