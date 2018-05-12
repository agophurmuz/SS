import methods.*;
import models.Particle;
import models.Position;

import java.io.FileOutputStream;
import java.util.*;

public class app {

    static final double L = 5.0;
    static final int W = 3;
    static final boolean open = true;
    static final double D = 0.5;

    public static void main(String[] args) {

        double particlesMass = 0.01;
        double maxDiameter = 0.03;
        double minDiameter = 0.02;

        int M = 1;
        //double rc = 2 * 0.1;
        double rc = maxDiameter;
        double k = 1E4;
        double gama = 5.0;
        //double gama = 2 * Math.sqrt(k * particlesMass);
        double totalTime = 5.0;
        //double deltaTime = 1E-4;
        double deltaTime = 3E-5;
        //double deltaTime = 0.1 * Math.sqrt(particlesMass/k);
        double delta2 = 100 * deltaTime;

        FileOutputStream fileOutputStream = FileGenerator.createOutputFilePoints("granular.xyz");
        List<Particle> particles = ParticleGenerator.generateParticles(particlesMass, minDiameter, maxDiameter, L, W, 10);
        CellIndexMethod method = new CellIndexMethod(false, M, L, rc, particles);
        Beeman beeman = new Beeman(new ForceCalculation(k, gama, deltaTime), deltaTime, L, W);
        double time = 0;
        int i = 0;

        while (time <= totalTime) {
            List<Particle> nextParticles = new ArrayList<>();
            Map<Particle, Set<Particle>> neighbors = method.getParticleNeighbors(particles);
            addWallParticleContact(neighbors, particles);
            if (i % 200 == 0) {
                FileGenerator.addHeader(fileOutputStream, particles.size());
            }
            for (Particle p : particles) {
                Particle nextP = beeman.moveParticle(p, neighbors.get(p));
                nextParticles.add(nextP);
                if (i % 200 == 0) {
                    FileGenerator.addParticle(fileOutputStream, nextP);
                }
            }

            if (i % 200 == 0) {
                FileGenerator.addWalls(fileOutputStream, particles.size(), particlesMass, L, W);
            }
            i++;


            particles = new ArrayList<>();
            particles.addAll(nextParticles);
            method.resetParticles(nextParticles);
            time += deltaTime;
        }
    }


    public static void addWallParticleContact(Map<Particle, Set<Particle>> neighbors, List<Particle> particles) {
        for (Particle p : particles) {
            Set<Particle> newParticles = getWallParticleContact(p);
            neighbors.get(p).addAll(newParticles);
        }
    }

    public static Set<Particle> getWallParticleContact(Particle particle) {
        Particle p;
        Set<Particle> result = new HashSet<>();
        if (particle.getX() - particle.getRadius() < 0) {
            // choco con pared Izq
            double x = -particle.getRadius();
            p = new Particle(111, new Position(x, particle.getY()), 0, 0, particle.getRadius(), particle.getMass());
            p.setWall(true);
            result.add(p);

            // choco con pared Derecha
        } else if (particle.getX() + particle.getRadius() >= W) {
            double x = W + particle.getRadius();
            p = new Particle(112, new Position(x, particle.getY()), 0, 0, particle.getRadius(), particle.getMass());
            p.setWall(true);
            result.add(p);


        }
        if (open) {
            if (particle.getY() - particle.getRadius() < (L / 10)) {
                if (particle.getX() - particle.getRadius() < (W / 2 - D / 2)) {
                    double y = -particle.getRadius();
                    p = new Particle(113, new Position(particle.getX(), y), 0, 0, particle.getRadius(), particle.getMass());
                    p.setWall(true);
                    result.add(p);
                } else if (particle.getX() + particle.getRadius() > (W / 2 + D / 2)) {
                    double y = -particle.getRadius();
                    p = new Particle(113, new Position(particle.getX(), y), 0, 0, particle.getRadius(), particle.getMass());
                    p.setWall(true);
                    result.add(p);
                }
            }
        } else {
            if (particle.getY() - particle.getRadius() < (L / 10)) {
                double y = -particle.getRadius();
                p = new Particle(113, new Position(particle.getX(), y), 0, 0, particle.getRadius(), particle.getMass());
                p.setWall(true);
                result.add(p);
            }
        }
        return result;
    }
}
