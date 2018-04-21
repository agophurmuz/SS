package itba.edu.ar.voyager;

import itba.edu.ar.models.Particle;
import itba.edu.ar.models.Position;

import java.util.List;

public class Body extends Particle {

    protected static final double G = 6.693 * 10E-11;

    public Body(double vX, double vY, Position position, double mass) {
        super(vX, vY, position, mass);
    }

    public double getAcceleration(List<Body> bodies) {
        double totalForce = getTotalForceReceivedFrom(bodies);
        return totalForce / mass;
    }

    private double getForceReceivedFrom(Body otherBody) {
        return (G * mass * otherBody.mass) / Math.pow(getEuclideanDistanceTo(otherBody), 2);
    }

    private double getTotalForceX(List<Body> bodies) {

        double total = 0;
        for (Body body : bodies) {
            total -= getForceReceivedFrom(body) * (position.getX() - body.position.getX()) / getEuclideanDistanceTo(body);
        }
        return total;
    }

    private double getTotalForceY(List<Body> bodies) {

        double total = 0;
        for (Body body : bodies) {
            total -= getForceReceivedFrom(body) * (position.getY() - body.position.getY()) / getEuclideanDistanceTo(body);
        }
        return total;
    }

    private double getTotalForceReceivedFrom(List<Body> bodies) {
        return Math.sqrt(Math.pow(getTotalForceX(bodies), 2) + Math.pow(getTotalForceY(bodies), 2));
    }


    private double getEuclideanDistanceTo(Body otherBody) {
        return position.getEuclideanDistance(otherBody.position);
    }

}
