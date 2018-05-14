import models.Particle;
import models.Position;

import java.util.ArrayList;
import java.util.List;


public class ParticleGenerator {

    private static int continuousInvalidLocations;
    private static final Integer MAX_PARTICLES = Integer.MAX_VALUE;
    private static final int MAX_TRIES = 10000;


    /**
     * Este método crea una cantidad X de particulas hasta que el silo está lleno.
     */
    public static List<Particle> generateParticles(double massParticle, double minDiameter,
                                            double maxDiameter, double L, double W) {
        return generateParticles(massParticle, minDiameter, maxDiameter, L, W, MAX_PARTICLES);
    }

    public static List<Particle> generateParticles(double massParticle, double minDiameter,
                                            double maxDiameter, double L, double W, Integer particleAmount) {
        List<Particle> particles = new ArrayList<>();
        double diameter = (Math.random() * (maxDiameter - minDiameter)) + minDiameter;
        double r = diameter / 2;
        double x1 = Math.random() * (W - (2 * r)) + r;
        double y1 = Math.random() * (L - (2 * r)) + r;
        double vx1 = 0;//Math.random();
        double vy1 = 0;//Math.random();
        particles.add(new Particle(0, new Position(x1, y1), vx1, vy1, r, massParticle));

        boolean flag = true;
        for (int i = 1; flag && particles.size() < particleAmount; i++) {
            continuousInvalidLocations = 0;
            //boolean validLocation = false;
            double x2 = 0;
            double y2 = 0;
            double r2 = 0;
            do {
                double diameter1 = (Math.random() * (maxDiameter - minDiameter)) + minDiameter;
                r2 = diameter1 / 2;
                x2 = Math.random() * (W - (2 * r2)) + r2;
                y2 = Math.random() * (L - (2 * r2)) + r2;
                continuousInvalidLocations++;
                if (continuousInvalidLocations == MAX_TRIES) {
                    flag = false;
                    break;
                }
            } while (isInvalidLocation(x2, y2, r2, particles));
            if (flag){
                particles.add(new Particle(i, new Position(x2, y2), 0, 0, r2, massParticle));
            }
        }
        return particles;
    }

    public static boolean isInvalidLocation(double x, double y, double r, List<Particle> particles) {
        for (Particle p : particles) {
            if((Math.pow(p.getX() - x, 2) + Math.pow(p.getY() - y, 2)) < Math.pow(p.getRadius() + r, 2)){
                return true;
            }
        }
        return false;
    }
}
