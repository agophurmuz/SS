import models.Particle;
import models.Position;

import java.util.ArrayList;
import java.util.List;

public class ParticleGenerator {

    public static List<Particle> particlesGenerator(double massParticle, double minDiameter,
                                                    double maxDiameter, int cantParticles, double L, double W) {
        List<Particle> particles = new ArrayList<>();

        double diameter = (Math.random() * (maxDiameter - minDiameter)) + minDiameter;
        double r = diameter / 2;
        double x1 = Math.random() * (W - (2 * r)) + r;
        double y1 = Math.random() * (L - (2 * r )) + r;
        double vx1 = Math.random(); //* 2 * v - v;
        double vy1 = Math.random(); //* 2 * v - v;
        particles.add(new Particle(0, new Position(x1, y1), vx1, vy1, 0.1, massParticle));

        for (int i = 1; i < cantParticles; i++) {
            boolean validLocation = false;
            double x2 = 0;
            double y2 = 0;
            while (!validLocation) {
                // ver si el x2 e y2 estan en el rango correcto
                //do {
                double diameter1 = (Math.random() * (maxDiameter - minDiameter)) + minDiameter;
                double r2 = diameter / 2;
                x2 = Math.random() * (W - (2 * r2)) + r2;
                y2 = Math.random() * (L - (2 * r2)) + r2;
                //}while (x2>L || y2>L || x2>0 || y2>0 );
                int j = 0;
                validLocation = true;
                while (j < particles.size() && validLocation) {
                    validLocation = isValidLocation(particles.get(j).getX(), particles.get(j).getY(),
                            particles.get(j).getRadius(), x2, y2, 0.1);
                    j++;
                }
            }
            double vx;
            double vy;
            vx = 0;//Math.random(); //* 2 * v - v;
            vy = 0;//Math.random(); //* 2 * v - v;

            particles.add(new Particle(i, new Position(x2, y2), vx, vy, 0.1, massParticle));
        }
        return particles;
    }

    private static boolean isValidLocation(double Xi, double Yi, double Ri, double Xj, double Yj, double Rj) {
        return (Math.pow(Xi - Xj, 2) + Math.pow(Yi - Yj, 2)) > Math.pow(Ri + Rj, 2);
    }
}
