package itba.edu.ar.models;

public class Particle {

    private Position position;
    private int id;

    public Particle(Position position, int id) {
        this.position = position;
        this.id = id;
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
}
