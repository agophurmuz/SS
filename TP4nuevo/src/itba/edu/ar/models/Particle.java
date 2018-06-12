package itba.edu.ar.models;

import java.util.Objects;

public class Particle {


    private int id;
    private Position position;
    private double vx;
    private double vy;
    private double mass;
    private double prevAccX;
    private double prevAccY;
    private double fx;
    private double fy;
    private ParticleType type;

    public Particle(int id, Position position, double vx, double vy, double mass, ParticleType type, double prevAccX, double prevAccY) {
        this.id = id;
        this.position = position;
        this.vx = vx;
        this.vy = vy;
        this.mass = mass;
        this.type = type;
        this.prevAccX = prevAccX;
        this.prevAccY = prevAccY;
    }

    public Particle(int id, Position position, double vx, double vy, double mass, ParticleType type) {
        this.id = id;
        this.position = position;
        this.vx = vx;
        this.vy = vy;
        this.mass = mass;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public double getFx() {
        return fx;
    }

    public void setForces(double fx, double fy) {
        this.fx = fx;
        this.fy = fy;
    }

    public double getFy() {
        return fy;
    }

    public double getPrevAccX() {
        return prevAccX;
    }

    public void setPrevAccX(double prevAccX) {
        this.prevAccX = prevAccX;
    }

    public double getPrevAccY() {
        return prevAccY;
    }

    public void setPrevAccY(double prevAccY) {
        this.prevAccY = prevAccY;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public double getVx() {
        return vx;
    }

    public void setVx(double vx) {
        this.vx = vx;
    }

    public double getVy() {
        return vy;
    }

    public void setVy(double vy) {
        this.vy = vy;
    }

    public double getMass() {
        return mass;
    }

    public double getX() {
        return position.getX().doubleValue();
    }

    public double getY() {
        return position.getY().doubleValue();
    }

    public ParticleType getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Particle)) return false;
        Particle particle = (Particle) o;
        return getId() == particle.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
