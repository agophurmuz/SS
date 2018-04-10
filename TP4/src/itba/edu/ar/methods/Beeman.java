package itba.edu.ar.methods;

public class Beeman {

    private double k;
    private double gama;
    private double r;
    private double v;
    private double mass;
    private double a;
    private double prevAcc;

    public Beeman(double k, double gama, double r0, double v0, double mass) {
        this.k = k;
        this.gama = gama;
        this.r = r0;
        this.v = v0;
        this.mass = mass;
        this.prevAcc = 0;
        this.a = calculateAcceleration();
    }

    public void oscillate(double totalTime, double deltaTime) {

        double time = 0;

        while (time < totalTime) {

            r = r + v * deltaTime + (2 * a * Math.pow(deltaTime, 2)) / 3 - (prevAcc * Math.pow(deltaTime, 2)) / 6;

            v = v + (3 * a * deltaTime) / 2 - (prevAcc * deltaTime) / 2;

            prevAcc = a;
            a = calculateAcceleration();
            time += deltaTime;

        }
    }

    private double calculateAcceleration() {
        return (-k * r -gama * v) / mass;
    }
}
