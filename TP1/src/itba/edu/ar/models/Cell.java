package itba.edu.ar.models;

import java.util.ArrayList;
import java.util.List;

public class Cell {

    private List<Particle> particleList = new ArrayList<>();

    public List<Particle> getParticleList() {
        return particleList;
    }

    public void addParticle(Particle particle) {
        this.particleList.add(particle);
    }

    @Override
    public String toString() {
        return "Cell{" +
                "particleList=" + particleList +
                '}'+"\n";
    }
}
