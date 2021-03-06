package itba.edu.ar.simulation.models;

import itba.edu.ar.simulation.methods.BorderType;

import java.util.Objects;

public class Particle {

    private Position position;
    private int id;
    private Double radius;
    private Double angle;
    private Double speed;
    private BorderType borderType;

    public Particle(Position position, int id, Double radius) {
        this.position = position;
        this.id = id;
        this.radius = radius;
    }

    public Particle(Position position, int id, Double radius, Double angle, Double speed) {
        this(position, id, radius);
        this.angle = angle;
        this.speed = speed;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Double getRadius() {
        return radius;
    }

    public void setRadius(Double radius) {
        this.radius = radius;
    }

    public Double getAngle() {
        return angle;
    }

    public void setAngle(Double angle) {
        this.angle = angle;
    }

    public Double getSpeed() {
        return speed;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    @Override
    public String toString() {
        return id + "\t" + position.toString();
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

    public BorderType getBorderType() {
        return borderType;
    }

    public void setBorderType(BorderType borderType) {
        this.borderType = borderType;
    }

    public double getX(){
        return position.getX().doubleValue();
    }

    public double getY(){
        return position.getY().doubleValue();
    }
}
