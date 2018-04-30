package models;

import methods.BorderType;

import java.util.ArrayList;
import java.util.List;

public class Cell {

    private List<Particle> particleList = new ArrayList<>();
    private BorderType borderType = BorderType.NO_BORDER;

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

    public BorderType getBorderType() {
        return borderType;
    }

    public void setBorderType(BorderType borderType) {
        this.borderType = borderType;
    }
}
