package models;

import generators.ColorGenerator;

import java.awt.*;
import java.util.Objects;

public class Particle {

    private int id;
    private Position position;
    private double vx;
    private double vy;
    private double radius;
    private double mass;
    private double prevAccX;
    private double prevAccY;
    private double fx;
    private double fy;
    private boolean isWall;
    private Color color;
    private float preasure;

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

    public void setForces(double fx, double fy) {
        this.fx = fx;
        this.fy = fy;
        this.color = calculateColor();
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

    public double getRadius() {
        return radius;
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

    @Override
    public String toString() {
        return id + "\t" + position.toString();
    }

    private Color calculateColor(){
        float minAngle = -240f/255; //corresponds to blue
        float maxAngle = -360f/255; //corresponds to red
        this.preasure = calculatePreasure();
        int zarlanga = (int)((255/200) * (preasure));
        //float angle = preasure*maxAngle + (1-preasure)*minAngle;
        //return new Color(Color.HSBtoRGB(angle, 1, 0.5f));
        return new Color((int) (preasure%255), 0,0);
    }

    private float calculatePreasure() {
        return (float) Math.sqrt(Math.pow(fx,2)+Math.pow((fy+(10*mass)),2)) / (float)(2*Math.PI*radius);
    }

    public int getRed(){
        return color.getRed();
    }

    public int getGreen(){
        return color.getGreen();
    }

    public int getBlue(){
        return color.getBlue();
    }

    public float getPreasure() {
        return preasure;
    }
}

