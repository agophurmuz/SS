package itba.edu.ar.models;

public class Particle {

    private double vX;
    private double vY;
    private Position position;
    private double mass;

    public Particle(double vX, double vY, Position position, double mass) {
        this.vX = vX;
        this.vY = vY;
        this.position = position;
        this.mass = mass;
    }

    public double getvX() {
        return vX;
    }

    public void setvX(double vX) {
        this.vX = vX;
    }

    public double getvY() {
        return vY;
    }

    public void setvY(double vY) {
        this.vY = vY;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public double getMass() {
        return mass;
    }

    public void setMass(double mass) {
        this.mass = mass;
    }
}
