package itba.edu.ar.simulation.models;

import java.util.ArrayList;
import java.util.List;

public class ParticleGenerator {

    public static List<Particle> particlesGenerator(double bigMassParticle, double smallMassParticle, double bigRadiusParticle,
                                                    double smallRadiusParticle, int cantParticles, double L, double v, double initX, double initY) {
        List<Particle> particles = new ArrayList<>();
        particles.add(new Particle(0, initX, initY, 0, 0, bigMassParticle, bigRadiusParticle));

        for (int i = 1; i <= cantParticles; i++) {
            boolean validLocation = false;
            double x2 = 0;
            double y2 = 0;
            while (!validLocation) {
                // ver si el x2 e y2 estan en el rango correcto
                //do {
                x2 = Math.random() * (L - (2 * smallRadiusParticle)) + smallRadiusParticle;
                y2 = Math.random() * (L - (2 * smallRadiusParticle)) + smallRadiusParticle;
                //}while (x2>L || y2>L || x2>0 || y2>0 );
                int j = 0;
                validLocation = true;
                while (j < particles.size() && validLocation) {
                    validLocation = isValidLocation(particles.get(j).getX(), particles.get(j).getY(), particles.get(j).getRadius(),
                            x2, y2, smallRadiusParticle);
                    j++;
                }
            }
            double vx;
            double vy;
            vx = Math.random() * 2 * v - v;
            vy = Math.random() * 2 * v - v;

            particles.add(new Particle(i, x2, y2, vx, vy, smallMassParticle, smallRadiusParticle));
        }
        return particles;
    }

    private static boolean isValidLocation(double Xi, double Yi, double Ri, double Xj, double Yj, double Rj) {
        return (Math.pow(Xi - Xj, 2) + Math.pow(Yi - Yj, 2)) > Math.pow(Ri + Rj, 2);
    }
}
