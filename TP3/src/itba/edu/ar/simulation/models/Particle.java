package itba.edu.ar.simulation.models;

public class Particle {

    private int id;
    private double x;
    private double y;
    private double vx;
    private double vy;
    private double mass;
    private double radius;

    public Particle(int id, double x, double y, double vx, double vy, double mass, double radius) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.mass = mass;
        this.radius = radius;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getVx() {
        return vx;
    }

    public void setVx(double vx) {
        this.vx = vx;
    }

    public double getVy() {
        return vy;
    }

    public void setVy(double vy) {
        this.vy = vy;
    }

    public double getMass() {
        return mass;
    }

    public void setMass(double mass) {
        this.mass = mass;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    @Override
    public String toString() {
        return id + "\t" + x + "\t" + y + "\t" + vx + "\t" + vy + "\t" + mass + "\t" + radius;
    }

    public void invertVx() {
        this.vx = -vx;
    }

    public void invertVy() {
        this.vy = -vy;
    }

    public void modifyVx(double j) {
        this.vx += (j/mass);
    }

    public void modifyVy(double j) {
        this.vy += (j/mass);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Particle particle = (Particle) o;

        if (id != particle.id) return false;
        if (Double.compare(particle.x, x) != 0) return false;
        if (Double.compare(particle.y, y) != 0) return false;
        if (Double.compare(particle.vx, vx) != 0) return false;
        if (Double.compare(particle.vy, vy) != 0) return false;
        if (Double.compare(particle.mass, mass) != 0) return false;
        return Double.compare(particle.radius, radius) == 0;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = id;
        temp = Double.doubleToLongBits(x);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(y);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(vx);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(vy);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(mass);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(radius);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
