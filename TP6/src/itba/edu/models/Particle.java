package itba.edu.models;

import javafx.scene.paint.Color;

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
    private float speed;
    private final static double MIN_SPEED_IN_COLOR_INTERVAL = 0.0 ;
    private final static double MAX_SPEED_IN_COLOR_INTERVAL = 2.0 ;
    private final static double GREEN_HUE = Color.GREEN.getHue() ;
    private final static double RED_HUE = Color.RED.getHue() ;
    private double desiredV;

    public Particle(int id, Position position, double vx, double vy, double radius, double mass, double desiredV) {
        this.id = id;
        this.position = position;
        this.vx = vx;
        this.vy = vy;
        this.radius = radius;
        this.mass = mass;
        this.prevAccX = 0;
        this.prevAccY = 0;
        this.desiredV = desiredV;
    }

    public Particle(int id, Position position, double vx, double vy, double radius, double mass, double prevAccX, double prevAccY, double desiredV) {
        this.id = id;
        this.position = position;
        this.vx = vx;
        this.vy = vy;
        this.radius = radius;
        this.mass = mass;
        this.prevAccX = prevAccX;
        this.prevAccY = prevAccY;
        this.desiredV = desiredV;
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
        this.speed = calculateSpeed();
        this.color = getColorForSpeed(speed);
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

    public boolean isWall() {
        return isWall;
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

    private Color getColorForSpeed(double preassure) {
        if (preassure > MAX_SPEED_IN_COLOR_INTERVAL) {
            return Color.DARKRED ;
        }
        double hue = GREEN_HUE + (RED_HUE - GREEN_HUE) * (preassure - MIN_SPEED_IN_COLOR_INTERVAL) / (MAX_SPEED_IN_COLOR_INTERVAL - MIN_SPEED_IN_COLOR_INTERVAL) ;
        return Color.hsb(hue, 1.0, 1.0);
    }


    private float calculateSpeed() {
        return (float) Math.sqrt(Math.pow(vx,2)+Math.pow((vy),2));
    }

    public double getRed() {
        return color.getRed();
    }

    public double getGreen(){
        return color.getGreen();
    }

    public double getBlue(){
        return color.getBlue();
    }

    public float getSpeed() {
        return speed;
    }

    public double getDesiredV() {
        return desiredV;
    }

    public void setDesiredV(double desiredV) {
        this.desiredV = desiredV;
    }

    public Position getPosition() {
        return position;
    }
}

