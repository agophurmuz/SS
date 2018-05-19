import methods.Beeman;
import methods.CellIndexMethod;
import models.Particle;
import models.Position;

import java.io.FileOutputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Silo {

    private List<Particle> particles;
    private CellIndexMethod cellIndexMethod;
    private Beeman beeman;
    private double totalTime;
    private double deltaTime;
    private double delta2;
    private int framesToPrint;
    private int caudal = 0;
    private double window = 0.1;
    private boolean open;
    private double L;
    private double W;
    private double D;
    private double particlesMass;
    private double minDiameter;
    private double maxDiameter;

    public Silo(List<Particle> particles, CellIndexMethod cellIndexMethod, Beeman beeman, double totalTime,
                double deltaTime, int framesToPrint, boolean open, double L, double W, double D, double particlesMass,
                double minDiameter, double maxDiameter, double delta2) {
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
        this.delta2 = delta2;
    }

    public void runSilo() {
        double time = 0;
        int i = 0;
        FileOutputStream fileOutputStream = FileGenerator.createOutputFilePoints("granular.xyz");
        ArrayList<Integer> caudals = new ArrayList<>();
        List<String> energyFileLog = new ArrayList<>();
        energyFileLog.add("Tiempo" + "\t" + "Energía" + "\t" + "Energía total");

        while (time <= totalTime) {
            //
            realocationParticles(particles);

            if (i % framesToPrint == 0) {
                FileGenerator.addHeader(fileOutputStream, particles.size());
                for (Particle p: particles) {
                    //System.out.println("Particula: " + p.getId() + " forceCalculation: " + p.getFx() + ", " + p.getFy());
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

            if (Math.abs(time / delta2 - Math.round(time / delta2)) < deltaTime) {
                System.out.println("Tiempo:"+time+"s, Caudal:"+caudal);
                caudals.add(caudal);
                double energy = computeKineticEnergy(particles);
                energyFileLog.add(Math.round(time * 1000.0) / 1000.0 + "\t" + energy + "\t" + (energy * particles.size()));
                caudal = 0;
            }

            cellIndexMethod.resetParticles(nextParticles);

            time += deltaTime;
            //System.out.println("Progress"+(time / totalTime)*100);
        }
        writeLogFileFromList(energyFileLog,"energy.tsv");
        writeLogFileFromList(proccesSlidingWindow(caudals,window,delta2),"caudals.tsv");
    }

    private void realocationParticles(List<Particle> particles) {
        for (Particle p : particles) {
            if (outOfSilo(p)) {
                realocatedParticle(p, particles);
                caudal+=1;

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
                //tries = 0;
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
                } else if (isAtRightOpeningBorder(particle) && getDistance(particle, (W / 2) - (D / 2), maxDiameter) < particle.getRadius()) {
                    double y = 0;
                    p = new Particle(cantParticles + 4, new Position((W / 2) - (D / 2), y), 0, 0, particle.getRadius(), particle.getMass());
                    p.setWall(true);
                    result.add(p);
                } else if (isAtLeftOpeningBorder(particle) && getDistance(particle, (W / 2) + (D / 2), maxDiameter) < particle.getRadius()) {
                    double y = 0;
                    p = new Particle(cantParticles + 5, new Position((W / 2) + (D / 2), y), 0, 0, particle.getRadius(), particle.getMass());
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

    private  List<String> proccesSlidingWindow(ArrayList<Integer> caudals, double window, double delta2) {
        int deltasInWindow = (int) (window/delta2);
        double average;
        int sum = 0;
        ArrayList<Integer> averagedCaudals = new ArrayList<>();
        List<String> caudalFileLog = new ArrayList<>();
        caudalFileLog.add("Tiempo"+ "\t" +"Caudal promediado");
        for(int i=0;i<caudals.size()-deltasInWindow;i++){
            for (int j=i;j<i+deltasInWindow;j++){
                sum+=caudals.get(j);
            }
            average = (double)sum/deltasInWindow;
            caudalFileLog.add(i*delta2+ "\t" +average);
            sum = 0;
        }
        return caudalFileLog;
    }

    private  void writeLogFileFromList(List<String> fileLog,String filename) {
        Path file = Paths.get(filename);
        try {
            Files.write(file, fileLog, Charset.forName("UTF-8"));
        } catch (Exception e) {

        }
    }
    private double computeKineticEnergy(List<Particle> particles) {
        double kineticEnergy = 0;
        for (Particle p : particles) {
            // 1/2*m*pow(v,2) where v is sqrt(pow(vx,2)+pow(vy,2))
            kineticEnergy += (1.0 / 2.0) * p.getMass() * (Math.pow(p.getVx(), 2) + Math.pow(p.getVy(), 2));
        }
        return kineticEnergy / particles.size();
    }

}
