package itba.edu.ar.models;

public abstract class PotentialCrash {


    protected double time;

    public PotentialCrash(double time) {
        this.time = time;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }
    public abstract boolean isWall();
}
