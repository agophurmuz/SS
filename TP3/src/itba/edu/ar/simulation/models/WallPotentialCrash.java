package itba.edu.ar.simulation.models;

public class WallPotentialCrash extends PotentialCrash {

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
                ", time=" + getTime() +
                '}';
    }

    public Particle getParticle() {
        return particle;
    }

    public void setParticle(Particle particle) {
        this.particle = particle;
    }

    public Wall getWall() {
        return wall;
    }

    public void setWall(Wall wall) {
        this.wall = wall;
    }

    @Override
    public void crash() {
        switch (wall) {
            case HORIZONTAL:
                particle.invertVy();
                break;
            case VERTICAL:
                particle.invertVx();
                break;
        }
    }

}
