import methods.*;
import models.Particle;
import models.Position;

import java.io.FileOutputStream;
import java.util.*;

public class appTest {

    static final int L = 5;
    static final int W = 2;
    static final boolean open = true;
    static final double D = 0.5;
    static final double l = 5.0;

    public static void main(String[] args) {

        double particlesMass = 0.01;
        double maxDiameter = 0.03;
        double minDiameter = 0.02;
        int cantParticles = 10;

        int M = 5;
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
        double l = 10.0;

        FileOutputStream fileOutputStream = FileGenerator.createOutputFilePoints("granular.xyz");
        List<Particle> particles = ParticleGenerator.particlesGenerator(particlesMass, minDiameter, maxDiameter, cantParticles, L, W);
        CellIndexMethod1 method = new CellIndexMethod1(l, rc, particles, false);
        Beeman beeman = new Beeman(new Force(k, gama, deltaTime), totalTime, deltaTime, L, W);
        double time = 0;
        int i = 0;

        while (time <= totalTime) {
            //TODO volver al iniicio en Y
            List<Particle> nextParticles = new ArrayList<>();
            Map<Particle, Set<Particle>> neighbors = method.findNeighbors(particles);
            //TODO chocar contra paredes
            addWallParticleContact(neighbors, particles);
            if(i%200 == 0) {
                FileGenerator.addHeader(fileOutputStream, particles.size());
            }
            for (Particle p : particles) {
                Particle nextP = beeman.moveParticle(p, neighbors.get(p));
                nextParticles.add(nextP);
                if(i%200 == 0) {
                    FileGenerator.addParticle(fileOutputStream, nextP);
                }
            }

            if(i%200 == 0) {
                FileGenerator.addWalls(fileOutputStream, particles.size(), particlesMass, L, W);
            }
            i++;

            particles = new ArrayList<>();
            particles.addAll(nextParticles);
            method.reloadMatrix(nextParticles);
            time += deltaTime;
        }
    }


    public static void addWallParticleContact(Map<Particle, Set<Particle>> neighbors, List<Particle> particles){
        for (Particle p : particles) {
            Set<Particle> newParticles = getWallParticleContact(p);
            neighbors.get(p).addAll(newParticles);
        }
    }

    public static Set<Particle> getWallParticleContact(Particle particle) {
        Particle p;
        Set<Particle> result = new HashSet<>();
        if (particle.getX() - particle.getRadius() < 0) {
            //fixme id particle, le estoy poniendo 0
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
            if (particle.getY() - particle.getRadius() < 0) {
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
            } /*else if(particle.getY() - particle.getRadius() < -(l/10)){
                particle.setOutOfSilo(true);
            }*/
        } else {
            if (particle.getY() - particle.getRadius() < 0) {
                double y = -particle.getRadius();
                p = new Particle(113, new Position(particle.getX(), y), 0, 0, particle.getRadius(), particle.getMass());
                p.setWall(true);
                result.add(p);
            }
        }
        return result;
    }
}
