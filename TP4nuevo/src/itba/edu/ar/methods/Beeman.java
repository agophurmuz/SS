package itba.edu.ar.methods;

import itba.edu.ar.models.Particle;
import itba.edu.ar.models.Position;

public class Beeman {

    private double deltaTime;

    public Beeman(double deltaTime) {
        this.deltaTime = deltaTime;
    }

    private double calculatePositionX(Particle particle) {
        return particle.getX() + particle.getVx() * deltaTime + (2 * calculateAccelerationX(particle) * Math.pow(deltaTime, 2)) / 3
                - (particle.getPrevAccX() * Math.pow(deltaTime, 2)) / 6;
    }

    private double calculatePositionY(Particle particle) {
        return particle.getY() + particle.getVy() * deltaTime + (2 * calculateAccelerationY(particle) * Math.pow(deltaTime, 2)) / 3
                - (particle.getPrevAccY() * Math.pow(deltaTime, 2)) / 6;
    }

    private double calculateAccelerationX(Particle particle) {
        return particle.getFx() / particle.getMass();
    }

    private double calculateAccelerationY(Particle particle) {
        return particle.getFy() / particle.getMass();
    }


    private double calculateVelocityX(Particle particle) {
        return particle.getVx() + ((calculateAccelerationX(particle) * deltaTime) / 3
                + (5 * calculateAccelerationX(particle) * deltaTime) / 6 - (particle.getPrevAccX() * deltaTime) / 6);
    }

    private double calculateVelocityY(Particle particle) {
        return particle.getVy() + ((calculateAccelerationY(particle) * deltaTime) / 3
                + (5 * calculateAccelerationY(particle) * deltaTime) / 6 - (particle.getPrevAccY() * deltaTime) / 6);
    }

    public Particle moveParticle(Particle p) {

        double x = calculatePositionX(p);
        double y = calculatePositionY(p);
        Position<Double> newPosition = new Position<>(x, y);

        double vx = calculateVelocityX(p);
        double vy = calculateVelocityY(p);

        double prevAccX = p.getFx() / p.getMass();
        double prevAccY = p.getFy() / p.getMass();

        return new Particle(p.getId(), newPosition, vx, vy, p.getMass(), p.getType(), prevAccX, prevAccY);
    }

}
