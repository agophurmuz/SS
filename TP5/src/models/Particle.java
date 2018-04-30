package models;

import methods.Force;

import java.util.Objects;

public class Particle {

    private int id;
    private Position position;
    private double vx;
    private double vy;
    private double radius;
    private double mass;
    private double a;
    private double prevAccX;
    private double prevAccY;
    private double fx;
    private double fy;
    private boolean isWall;

    public Particle(int id, Position position, double vx, double vy, double radius, double mass) {
        this.id = id;
        this.position = position;
        this.vx = vx;
        this.vy = vy;
        this.radius = radius;
        this.mass = mass;
        this.prevAccX = 0;
        this.prevAccY = 0;
    }

    public Particle(int id, Position position, double vx, double vy, double radius, double mass, double prevAccX, double prevAccY) {
        this.id = id;
        this.position = position;
        this.vx = vx;
        this.vy = vy;
        this.radius = radius;
        this.mass = mass;
        this.prevAccX = prevAccX;
        this.prevAccY = prevAccY;
    }

    public int getId() {
        return id;
    }

    public double getFx() {
        return fx;
    }

    public void setFx(double fx) {
        this.fx = fx;
    }

    public double getFy() {
        return fy;
    }

    public void setFy(double fy) {
        this.fy = fy;
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

    public Position getPosition() {
        return position;
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

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public double getMass() {
        return mass;
    }

    public void setMass(double mass) {
        this.mass = mass;
    }

    public double getX(){
        return position.getX().doubleValue();
    }

    public double getY(){
        return position.getY().doubleValue();
    }

    public boolean isWall() {
        return isWall;
    }

    public void setWall(boolean wall) {
        isWall = wall;
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

