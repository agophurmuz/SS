package itba.edu.ar.simulation.models;

public abstract class PotentialCrash {


    private final double time;

    public PotentialCrash(double time) {

        this.time = time;
    }

    public double getTime() {
        return time;
    }

    public abstract void crash();
}
