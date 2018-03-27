package itba.edu.ar.simulation.models;

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
                ", time=" + getTime() +
                '}';
    }

    public Particle getParticle1() {
        return particle1;
    }

    public void setParticle1(Particle particle1) {
        this.particle1 = particle1;
    }

    public Particle getParticle2() {
        return particle2;
    }

    public void setParticle2(Particle particle2) {
        this.particle2 = particle2;
    }

    public double getImpulse() {
        double mass1 = particle1.getMass();
        double mass2 = particle2.getMass();
        return (2 * mass1 * mass2 * getVR()) / (getSigma() * (mass1 + mass2));

    }

    private double getDeltaX() {
        return particle2.getX() - particle1.getX();
    }

    private double getDeltaY() {
        return particle2.getY() - particle1.getY();
    }

    private double getVR() {
        double deltaVx = particle2.getVx() - particle1.getVx();
        double deltaVy = particle2.getVy() - particle1.getVy();

        return deltaVx * getDeltaX() + deltaVy * getDeltaY();
    }

    private double getSigma() {
        return particle2.getRadius() + particle1.getRadius();
    }

    public double getImpulseX() {
        return (getImpulse() * getDeltaX()) / getSigma();
    }

    public double getImpulseY() {
        return (getImpulse() * getDeltaY()) / getSigma();
    }

    @Override
    public void crash() {
        final double impulseX = getImpulseX();
        final double impulseY = getImpulseY();
        particle1.modifyVx(impulseX);
        particle1.modifyVy(impulseY);
        particle2.modifyVx(-impulseX);
        particle2.modifyVy(-impulseY);
    }
}
