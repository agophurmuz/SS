package itba.edu.ar.models;

public class Particle {

    private Position position;
    private int id;
    private Double radius;

    public Particle(Position position, int id, Double radius) {
        this.position = position;
        this.id = id;
        this.radius = radius;
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

    @Override
    public String toString() {
        return "Particle{" +
                position +
                ", id=" + id +
                ", radius=" + radius +
                '}'+"\n";
    }
}
