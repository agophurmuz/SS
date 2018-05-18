import methods.*;
import models.Particle;
import models.Position;

import java.io.FileOutputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class app {

    static final double L = 0.5;
    static final double W = 0.4;
    static final boolean open = true;
    static final double D = 0.16; //0.15, 0.18, 0.21, 0.24
    static final int framesToPrint = 200;
    static final double maxDiameter = 0.03;
    static final double minDiameter = 0.02;
    static final double EPSILON = 0.00001;
    static int caudal = 0;

    public static void main(String[] args) {

        double particlesMass = 0.01;

        int M = 3;
        //double rc = 2 * 0.1;
        double rc = maxDiameter;
        double k = 1E5;
        //double gama = 20.0;
        double gama = 2 * Math.sqrt(k * particlesMass);
        double totalTime = 5.0;
        //double deltaTime = 1E-4;
        double deltaTime = 1E-5;
        //double deltaTime = 0.1 * Math.sqrt(particlesMass/k);
        //double delta2 = 0.02;
        double delta2 = 0.1;
        boolean energyPrint = true;
        boolean caudalPrint = false;
        boolean simulationPrint = true;

        FileOutputStream fileOutputStream = FileGenerator.createOutputFilePoints("granular.xyz");
        List<String> energyFileLog = new ArrayList<String>();
        energyFileLog.add("Tiempo" + "\t" + "Energía" + "\t" + "Energía total");
        List<String> caudalFileLog = new ArrayList<String>();
        caudalFileLog.add("Tiempo" + "\t" + "Caudal");
        List<Particle> particles = ParticleGenerator.generateParticles(particlesMass, minDiameter, maxDiameter, L, W);
        FileGenerator.addHeader(fileOutputStream, particles.size());

        for (Particle p : particles) {
            FileGenerator.addParticle(fileOutputStream, p);
        }
        FileGenerator.addWalls(fileOutputStream, particles.size(), particlesMass, L, W);
        CellIndexMethod method = new CellIndexMethod(false, M, L, rc, particles);
        Beeman beeman = new Beeman(new ForceCalculation(k, gama, deltaTime), deltaTime, L, W);
        double time = 0;
        int i = 0;

        while (time <= totalTime) {
            particles = realocationParticles(particles);
            List<Particle> nextParticles = new ArrayList<>();
            Map<Particle, Set<Particle>> neighbors = method.getParticleNeighbors(particles);
            addWallParticleContact(neighbors, particles);
            if (i % framesToPrint == 0) {
                FileGenerator.addHeader(fileOutputStream, particles.size());
            }
            for (Particle p : particles) {
                Particle nextP = beeman.moveParticle(p, neighbors.get(p), nextParticles);
                nextParticles.add(nextP);
                if (i % framesToPrint == 0) {
                    FileGenerator.addParticle(fileOutputStream, nextP);
                }
            }

            if (i % framesToPrint == 0) {
                FileGenerator.addWalls(fileOutputStream, particles.size(), particlesMass, L, W);
            }
            i++;

            particles = new ArrayList<>();
            particles.addAll(nextParticles);
            method.resetParticles(nextParticles);

            if ((Math.abs(time / delta2 - Math.round(time / delta2)) < EPSILON) && energyPrint) {
                double energy = computeKineticEnergy(particles);
                energyFileLog.add(Math.round(time * 1000.0) / 1000.0 + "\t" + energy + "\t" + (energy * particles.size()));
                caudalFileLog.add(Math.round(time * 1000.0) / 1000.0 + "\t" + caudal);
            }

            time += deltaTime;
            System.out.println(time / totalTime);
        }
        writeKineticEnergyLogFile(energyFileLog);
        writeCaudalLogFile(caudalFileLog);
    }

    private static List<Particle> realocationParticles(List<Particle> particles) {
        Set<Particle> newParticles = new HashSet<>(particles);
        for (Particle p : particles) {
            if (outOfSilo(p)) {
                newParticles.remove(p);
                Particle realocParticle = realocatedParticle(p, newParticles);
                newParticles.add(realocParticle);
                caudal++;
            }
        }
        return new ArrayList<>(newParticles);
    }

    private static Particle realocatedParticle(Particle p, Set<Particle> particles) {
        double x,y;
        int tries = 0;
        do{
            x = Math.random() * (W - (2 * p.getRadius()) + p.getRadius()+ 0.01);

            Particle maxHeightParticle =getMaxHeightParticle(particles, x, p.getRadius());
            if (tries>=10000 || maxHeightParticle == null ){
                y = L*1.4+Math.random()*0.2 + p.getRadius();
                break;
            }else{
                y = Math.min((Math.random()*L*0.25+0.9*L),maxHeightParticle.getY()+ maxHeightParticle.getRadius());
            }

            tries++;
        }while(ParticleGenerator.isInvalidLocation(x,y,p.getRadius(),new ArrayList<>(particles)) );

        return new Particle(p.getId(), new Position(x, y), 0, 0, p.getRadius(), p.getMass(), 0, 0);

    }

    private static Particle getMaxHeightParticle(Set<Particle> particles, double x, double r) {
        double aux = 0;
        Particle p = null;
        for (Particle particle : particles) {
            if (Math.abs(particle.getX() - x) < particle.getRadius() + r && particle.getY() > aux) {
                aux = particle.getY();
                p = particle;
            }
        }
        return p;
    }

    private static boolean outOfSilo(Particle p) {
        return (p.getY() - p.getRadius()) < -(L / 10);
    }


    public static void addWallParticleContact(Map<Particle, Set<Particle>> neighbors, List<Particle> particles) {
        for (Particle p : particles) {
            Set<Particle> newParticles = getWallParticleContact(p, particles.size());
            neighbors.get(p).addAll(newParticles);
        }
    }

    public static Set<Particle> getWallParticleContact(Particle particle, int cantParticles) {
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
                } else if (isAtRightOpeningBorder(particle) && getDistance(particle, (W / 2) - (D / 2), maxDiameter) < particle.getRadius()) {
                    double y = maxDiameter;
                    p = new Particle(cantParticles + 4, new Position((W / 2) - (D / 2), y), 0, 0, 0, particle.getMass());
                    p.setWall(true);
                    result.add(p);
                } else if (isAtLeftOpeningBorder(particle) && getDistance(particle, (W / 2) + (D / 2), maxDiameter) < particle.getRadius()) {
                    double y = maxDiameter;
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

    private static boolean isAtLeftOpeningBorder(Particle particle) {
        return particle.getX() + particle.getRadius() >= ((W / 2) + (D / 2));
    }

    private static boolean isAtRightOpeningBorder(Particle particle) {
        return particle.getX() - particle.getRadius() <= ((W / 2) - (D / 2));
    }

    private static boolean isLeftOpeningFloor(Particle particle) {
        return particle.getX() >= ((W / 2) + (D / 2));
    }

    private static boolean isRightOpeningFloor(Particle particle) {
        return particle.getX() <= ((W / 2) - (D / 2));
    }

    private static double getDistance(Particle particle, double x, double y) {
        return Math.sqrt(Math.pow(particle.getX() - x, 2) + Math.pow(particle.getY() - y, 2));
    }

    private static double computeKineticEnergy(List<Particle> particles) {
        double kineticEnergy = 0;
        for (Particle p : particles) {
            // 1/2*m*pow(v,2) where v is sqrt(pow(vx,2)+pow(vy,2))
            kineticEnergy += (1.0 / 2.0) * p.getMass() * (Math.pow(p.getVx(), 2) + Math.pow(p.getVy(), 2));
        }
        return kineticEnergy / particles.size();
    }

    private static void writeKineticEnergyLogFile(List<String> lines) {
        Path file = Paths.get("energy.tsv");
        try {
            Files.write(file, lines, Charset.forName("UTF-8"));
        } catch (Exception e) {

        }
    }

    private static void writeCaudalLogFile(List<String> caudalFileLog) {
        Path file = Paths.get("caudal.tsv");
        try {
            Files.write(file, caudalFileLog, Charset.forName("UTF-8"));
        } catch (Exception e) {

        }
    }

}
