package itba.edu.ar.models;

import java.util.ArrayList;
import java.util.List;

public class Cell {

    private List<Particle> patricleList;

    public Cell() {
        this.patricleList = new ArrayList<>();
    }

    public List<Particle> getPatricleList() {
        return patricleList;
    }

    public void addParticle(Particle particle) {
        this.patricleList.add(particle);
    }
}
