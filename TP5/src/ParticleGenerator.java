import models.Particle;
import models.Position;

import java.util.ArrayList;
import java.util.List;


public class ParticleGenerator {

    private static int continuousInvalidLocations;
    private static final Integer MAX_PARTICLES = 20;


    /**
     * Este método crea una cantidad X de particulas hasta que el cilo está lleno.
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
        double y1 = Math.random() * ((L - L / 10) - (2 * r)) + r + (L / 10);
        double vx1 = Math.random();
        double vy1 = Math.random();
        particles.add(new Particle(0, new Position(x1, y1), vx1, vy1, r, massParticle));

        for (int i = 1; reachedCutCondition(i,particleAmount); i++) {
            continuousInvalidLocations = 0;
            boolean validLocation = false;
            System.out.println("Particles: " + i);
            double x2 = 0;
            double y2 = 0;
            double r2 = 0;
            while (!isFullDomain() && !validLocation) {
                continuousInvalidLocations++;
                System.out.println(continuousInvalidLocations);
                // Se chequea si el x2 e y2 estan en el rango correcto
                double diameter1 = (Math.random() * (maxDiameter - minDiameter)) + minDiameter;
                r2 = diameter1 / 2;
                x2 = Math.random() * (W - (2 * r2)) + r2;
                y2 = Math.random() * ((L - L / 10) - (2 * r2)) + r2 + (L / 10);
                int j = 0;
                validLocation = true;
                while (j < particles.size() && validLocation) {
                    validLocation = isValidLocation(particles.get(j).getX(), particles.get(j).getY(),
                            particles.get(j).getRadius(), x2, y2, r2);
                    j++;
                }
            }
            double vx;
            double vy;
            vx = Math.random();
            vy = Math.random();

            particles.add(new Particle(i, new Position(x2, y2), vx, vy, r2, massParticle));
        }
        return particles;
    }

    private static boolean isValidLocation(double Xi, double Yi, double Ri, double Xj, double Yj, double Rj) {
        return (Math.pow(Xi - Xj, 2) + Math.pow(Yi - Yj, 2)) > Math.pow(Ri + Rj, 2);
    }

    private static boolean isFullDomain() {
        return continuousInvalidLocations > 3;
    }

    private static boolean reachedCutCondition(int i, int particleAmount) {
        return !isFullDomain() && i < particleAmount;
    }
}
