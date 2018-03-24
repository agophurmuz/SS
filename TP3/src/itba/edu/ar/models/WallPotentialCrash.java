package itba.edu.ar.models;

public class WallPotentialCrash extends PotentialCrash{

    Particle particle;
    Wall wall;

    public WallPotentialCrash(double time, Particle particle, Wall wall) {
        super(time);
        this.particle = particle;
        this.wall = wall;
    }

    @Override
    public String toString() {
        return "WallPotentialCrash{" +
                "particle=" + particle +
                ", wall=" + wall +
                ", time=" + time +
                '}';
    }
}
