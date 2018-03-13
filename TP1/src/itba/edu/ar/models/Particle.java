package itba.edu.ar.models;

public class Particle {

    private Position position;
    private int id;
    private Double radius;
    private Double angle;
    private Double speed;

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
        return "Particle{" +
                position +
                ", id=" + id +
                ", radius=" + radius +
                '}'+"\n";
    }
}
