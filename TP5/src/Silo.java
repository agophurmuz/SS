import methods.Beeman;
import methods.CellIndexMethod;
import models.Particle;
import models.Position;

import java.io.FileOutputStream;
import java.util.*;

public class Silo {

    private List<Particle> particles;
    private CellIndexMethod cellIndexMethod;
    private Beeman beeman;
    private double totalTime;
    private double deltaTime;
    private int framesToPrint;
    private  int caudal = 0;
    private boolean open;
    private double L;
    private double W;
    private double D;
    private double particlesMass;
    private double minDiameter;
    private double maxDiameter;

    public Silo(List<Particle> particles, CellIndexMethod cellIndexMethod, Beeman beeman, double totalTime,
                double deltaTime, int framesToPrint, boolean open, double L, double W, double D, double particlesMass,
                double minDiameter, double maxDiameter) {
        this.particles = particles;
        this.cellIndexMethod = cellIndexMethod;
        this.beeman = beeman;
        this.totalTime = totalTime;
        this.deltaTime = deltaTime;
        this.framesToPrint = framesToPrint;
        this.open = open;
        this.L = L;
        this.W = W;
        this.D = D;
        this.particlesMass = particlesMass;
        this.minDiameter = minDiameter;
        this.maxDiameter = maxDiameter;
    }

    public void runSilo() {
        double time = 0;
        int i = 0;
        FileOutputStream fileOutputStream = FileGenerator.createOutputFilePoints("granular.xyz");

        while (time <= totalTime) {
            realocationParticles(particles);
            if (i % framesToPrint == 0) {
                FileGenerator.addHeader(fileOutputStream, particles.size());
                for (Particle p: particles) {
                    System.out.println("Particula: " + p.getId() + " forceCalculation: " + p.getFx() + ", " + p.getFy());
                    FileGenerator.addParticle(fileOutputStream, p);
                }
                //FileGenerator.addWalls(fileOutputStream, particles.size(), particlesMass, L, W);
            }

            List<Particle> nextParticles = new ArrayList<>();
            Map<Particle, Set<Particle>> neighbors = cellIndexMethod.getParticleNeighbors(particles);
            addWallParticleContact(neighbors);
            for (Particle p : particles) {
                Particle nextP = beeman.moveParticle(p, neighbors.get(p), nextParticles);
                nextParticles.add(nextP);
            }

            i++;

            particles = new ArrayList<>();
            particles.addAll(nextParticles);
            cellIndexMethod.resetParticles(nextParticles);

            time += deltaTime;
            System.out.println(time / totalTime);
        }
    }

    private void realocationParticles(List<Particle> particles) {
        for (Particle p : particles) {
            if (outOfSilo(p)) {
                realocatedParticle(p, particles);
                caudal++;
            }
        }
    }

    private void realocatedParticle(Particle p, List<Particle> particles) {
        double x,y;
        int tries = 0;
        do {
            x = (Math.random() * (W - maxDiameter - maxDiameter)) + maxDiameter;
            y = Math.random() * 0.5 * L + 0.5*L;
            if(tries == 1000){
                break;
            }
            tries++;
        } while (ParticleGenerator.isInvalidLocation(x, y, p.getRadius(), particles));
        p.setPosition(new Position(x, y));
        p.setVx(0);
        p.setVy(0);
        p.setPrevAccX(0);
        p.setPrevAccY(0);
    }

    private boolean outOfSilo(Particle p) {
        return (p.getY() - p.getRadius()) < -(L / 10);
    }

    private void addWallParticleContact(Map<Particle, Set<Particle>> neighbors) {
        Set<Particle> particles = neighbors.keySet();
        for (Particle p : particles) {
            Set<Particle> newParticles = getWallParticlesContact(p, particles.size());
            neighbors.get(p).addAll(newParticles);
        }
    }

    private Set<Particle> getWallParticlesContact(Particle particle, int cantParticles) {
        Particle p;
        Set<Particle> result = new HashSet<>();
        if (particle.getX() - particle.getRadius() < 0) {
            // choco con pared Izq
            double x = -particle.getRadius();
            p = new Particle(cantParticles + 1, new Position(x, particle.getY()), 0, 0, particle.getRadius(), particle.getMass());
            p.setWall(true);
            result.add(p);

            // choco con pared Derecha
        } else if (particle.getX() + particle.getRadius() >= W) {
            double x = W + particle.getRadius();
            p = new Particle(cantParticles + 2, new Position(x, particle.getY()), 0, 0, particle.getRadius(), particle.getMass());
            p.setWall(true);
            result.add(p);


        }
        if (open) {
            if (particle.getY() - particle.getRadius() < 0) {
                if (isRightOpeningFloor(particle) || isLeftOpeningFloor(particle)) {
                    double y = -particle.getRadius();
                    p = new Particle(cantParticles + 3, new Position(particle.getX(), y), 0, 0, particle.getRadius(), particle.getMass());
                    p.setWall(true);
                    result.add(p);
                } else if (isAtRightOpeningBorder(particle) && getDistance(particle, (W / 2) - (D / 2), 0) < particle.getRadius()) {
                    double y = 0;
                    p = new Particle(cantParticles + 4, new Position((W / 2) - (D / 2), y), 0, 0, 0, particle.getMass());
                    p.setWall(true);
                    result.add(p);
                } else if (isAtLeftOpeningBorder(particle) && getDistance(particle, (W / 2) + (D / 2), 0) < particle.getRadius()) {
                    double y = 0;
                    p = new Particle(cantParticles + 5, new Position((W / 2) + (D / 2), y), 0, 0, 0, particle.getMass());
                    p.setWall(true);
                    result.add(p);
                }
            }
        } else {
            if (particle.getY() - particle.getRadius() < 0) {
                double y = -particle.getRadius();
                p = new Particle(cantParticles + 6, new Position(particle.getX(), y), 0, 0, particle.getRadius(), particle.getMass());
                p.setWall(true);
                result.add(p);
            }
        }
        return result;
    }

    private boolean isAtLeftOpeningBorder(Particle particle) {
        return particle.getX() + particle.getRadius() >= ((W / 2) + (D / 2));
    }

    private boolean isAtRightOpeningBorder(Particle particle) {
        return particle.getX() - particle.getRadius() <= ((W / 2) - (D / 2));
    }

    private boolean isLeftOpeningFloor(Particle particle) {
        return particle.getX() >= ((W / 2) + (D / 2));
    }

    private boolean isRightOpeningFloor(Particle particle) {
        return particle.getX() <= ((W / 2) - (D / 2));
    }

    private double getDistance(Particle particle, double x, double y) {
        return Math.sqrt(Math.pow(particle.getX() - x, 2) + Math.pow(particle.getY() - y, 2));
    }

}
