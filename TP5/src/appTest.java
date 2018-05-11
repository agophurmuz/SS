import methods.*;
import models.Particle;
import models.Position;

import java.io.FileOutputStream;
import java.util.*;

public class appTest {

    static final int L = 10;
    static final int W = 5;

    public static void main(String[] args) {

        double particlesMass = 0.01;
        double maxDiameter = 0.3;
        double minDiameter = 0.2;
        int cantParticles = 100;

        int D = 3;
        int M = 5;
        double rc = 2 * 0.1;
        double k = 1E4;
        double gama = 14.0;
        //double gama = 2 * Math.sqrt(k * particlesMass);
        double totalTime = 5.0;
        //double deltaTime = 1E-4;
        double deltaTime = 3E-5;
        //double deltaTime = 0.1 * Math.sqrt(particlesMass/k);
        double delta2 = 100 * deltaTime;
        double l = 10.0;

        FileOutputStream fileOutputStream = FileGenerator.createOutputFilePoints("granular.xyz");
        FileOutputStream fileOutputStream1 = FileGenerator.createOutputFilePoints("neighbors.xyz");
        //List<Particle> particles = ParticleGenerator.particlesGenerator(particlesMass, D/7, D/5, cantParticles, L, W);
        List<Particle> particles = new ArrayList<>();
        particles.add(new Particle(0, new Position(3.0, 0.5), 0, -0.8, 0.1, particlesMass));
        particles.add(new Particle(1, new Position(3.5, 0.3), 0, -0.8, 0.1, particlesMass));
        //FileGenerator.addHeader(fileOutputStream, particles.size());
        //for (Particle p : particles) {
        //    FileGenerator.addParticle(fileOutputStream, p);
        //}
        //FileGenerator.addWalls(fileOutputStream, particles.size(), particlesMass, L, W);
        //CellIndexMethod method = new CellIndexMethod(BoundaryCondition.NON_PERIODIC, M, L, rc, particles, W);
        CellIndexMethod1 method = new CellIndexMethod1(l, rc, particles, false);
        Beeman beeman = new Beeman(new Force(k, gama, deltaTime), totalTime, deltaTime, L, W);
        double time = 0;
        //Particle particle1 = new Particle(0, new Position(1,5), 0.02, 0, 1, particlesMass);
        //Particle particle2 = new Particle(1, new Position(2,5), -0.02, 0, 1, particlesMass);
        //FileGenerator.addParticle(fileOutputStream, particle1);
        //FileGenerator.addParticle(fileOutputStream, particle2);
        int i = 0;
        /*Map<Particle, Set<Particle>> neighbors = method.getParticleNeighbors();
        for (Particle p : neighbors.keySet()) {
            FileGenerator.addPointsToFile(fileOutputStream1, neighbors, p);
        }*/

        while (time <= totalTime) {
            //TODO volver al iniicio en Y
            List<Particle> nextParticles = new ArrayList<>();
            Map<Particle, Set<Particle>> neighbors = method.findNeighbors(particles);
            /*for (Particle p : neighbors.keySet()) {
                FileGenerator.addPointsToFile(fileOutputStream1, neighbors, p);
            }*/
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
                //FileGenerator.addPointsToFile(fileOutputStream1, neighbors, p);
            }
            //FileGenerator.addParticle(fileOutputStream, particle1);
            //FileGenerator.addParticle(fileOutputStream, particle2);
            //Set<Particle> set1 = new HashSet<>();
            //set1.add(particle2);
            //Set<Particle> set2 = new HashSet<>();
            //set2.add(particle1);
            //particle1 = beeman.moveParticle(particle1, set1);
            //particle2 = beeman.moveParticle(particle2, set2);

            if(i%200 == 0) {
                FileGenerator.addWalls(fileOutputStream, particles.size(), particlesMass, L, W);
            }
            i++;

            particles = new ArrayList<>();
            particles.addAll(nextParticles);
            method.reloadMatrix(nextParticles);
            time += deltaTime;
        }
        //FileGenerator.addCorners(fileOutputStream, particles.size(), L);
        //System.out.println(time);
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
        if(particle.getX() - particle.getRadius() < 0) {
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

            // choco con pared piso
        } else if (particle.getY() - particle.getRadius() < 0) {
            double y = -particle.getRadius();
            p = new Particle(113, new Position(particle.getX(), y), 0, 0, particle.getRadius(), particle.getMass());
            p.setWall(true);
            result.add(p);

            // choco con pared techo
        } else if (particle.getY() + particle.getRadius() >= L) {
            double y = L + particle.getRadius();
            p = new Particle(114, new Position(particle.getX(), y), 0, 0, particle.getRadius(), particle.getMass());
            p.setWall(true);
            result.add(p);
        }

        return result;
    }
}
