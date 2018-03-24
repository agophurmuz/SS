package itba.edu.ar.models;

public class ParticlePotentialCrash extends PotentialCrash {

    private Particle particle1;
    private Particle particle2;

    public ParticlePotentialCrash(double time, Particle particle1, Particle particle2) {
        super(time);
        this.particle1 = particle1;
        this.particle2 = particle2;
    }

    @Override
    public String toString() {
        return "ParticlePotentialCrash{" +
                "particle1=" + particle1 +
                ", particle2=" + particle2 +
                ", time=" + time +
                '}';
    }
}
