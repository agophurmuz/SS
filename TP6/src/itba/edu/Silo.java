package itba.edu;

import itba.edu.methods.Beeman;
import itba.edu.methods.CellIndexMethod;
import itba.edu.methods.ForceCalculation;
import itba.edu.models.Particle;
import itba.edu.models.Position;

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
    private int exitedParticules = 0;
    private double window = 0.1;
    private boolean open;
    private double L;
    private double W;
    private double D;

    private double maxDiameter;
    private ForceCalculation forceCalculation;

    public Silo(ForceCalculation forceCalculation, List<Particle> particles, CellIndexMethod cellIndexMethod, Beeman beeman, double totalTime,
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
        this.maxDiameter = maxDiameter;
        this.delta2 = delta2;
        this.forceCalculation = forceCalculation;
    }

    public void runSilo() {
        double time = 0;
        int i = 0;
        FileOutputStream fileOutputStream = FileGenerator.createOutputFilePoints("granular.xyz");
        ArrayList<Integer> caudals = new ArrayList<>();
        List<String> energyFileLog = new ArrayList<>();
        energyFileLog.add("Tiempo" + "\t" + "Energía" + "\t" + "Energía total");

        while (time <= totalTime && !particles.isEmpty()) {

            removeExitedParticles();

            List<Particle> nextParticles = new ArrayList<>();

            // Calculamos vecinos para cada particula.
            Map<Particle, Set<Particle>> neighbors = cellIndexMethod.getParticleNeighbors(particles);

            // Agregamos particulas ficticias en caso de contacto con paredes y bordes.
            addWallParticleContact(neighbors);

            // Calculamos las fuerzas a las que están sometidas las particulas.
            calculateParticlesForces(neighbors);

            // Printeamos estado actual.
            printActualState(fileOutputStream, i);

            // Log caudals with sliding window 
            //logCaudalAndEnergy(time,caudals,energyFileLog);

            //Movemos las partículas al siguiente estado.
            moveParticles(neighbors, nextParticles);


            //Actualizamos particulas con los nuevos estados.
            updateParticesState(nextParticles);

            i++;
            time += deltaTime;
        }
        writeLogFileFromList(energyFileLog,"energy.tsv");
        writeLogFileFromList(proccesSlidingWindow(caudals,window,delta2),"caudals.tsv");
    }

    private void removeExitedParticles() {
        List <Particle> particlesInRoom = new ArrayList<>();
        for (Particle p : particles) {
            if (!outOfSilo(p)) {
                particlesInRoom.add(p);
            }else{
                exitedParticules ++;
            }
        }
        particles = particlesInRoom;
    }

    private void updateParticesState(List<Particle> nextParticles) {
        particles = new ArrayList<>();
        particles.addAll(nextParticles);
        cellIndexMethod.resetParticles(nextParticles);
    }

    private void moveParticles(Map<Particle, Set<Particle>> neighbors, List<Particle> nextParticles ) {
        for (Particle p : particles) {
            Particle nextP = beeman.moveParticle(p, neighbors.get(p), nextParticles);
            nextParticles.add(nextP);
        }
    }

    private void printActualState(FileOutputStream fileOutputStream, int i) {
        if (i % framesToPrint == 0) {
            FileGenerator.addHeader(fileOutputStream, particles.size());
            for (Particle p: particles) {
                FileGenerator.addParticle(fileOutputStream, p);
            }
            FileGenerator.addWalls(fileOutputStream,particles.size(),L,W);
        }
    }

    private void calculateParticlesForces(Map<Particle, Set<Particle>> neighbors) {
        for (Particle p : particles) {
            forceCalculation.setForces(p, neighbors.get(p));
        }
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
            p = new Particle(cantParticles + 1, new Position(x, particle.getY()), 0, 0, particle.getRadius(), particle.getMass(), 0);
            p.setWall(true);
            result.add(p);

            // choco con pared Derecha
        } else if (particle.getX() + particle.getRadius() >= W) {
            double x = W + particle.getRadius();
            p = new Particle(cantParticles + 2, new Position(x, particle.getY()), 0, 0, particle.getRadius(), particle.getMass(), 0);
            p.setWall(true);
            result.add(p);
        }else if (particle.getY() + particle.getRadius() >= L){
            //Choco con el techo
            addWallParticle(result,particle,cantParticles,particle.getX(),L+particle.getRadius());
        }
        if (open) {
            if (particle.getY() - particle.getRadius() < 0) {
                if (isRightOpeningFloor(particle) || isLeftOpeningFloor(particle)) {
                    double y = -particle.getRadius();
                    p = new Particle(cantParticles + 3, new Position(particle.getX(), y), 0, 0, particle.getRadius(), particle.getMass(), 0);
                    p.setWall(true);
                    result.add(p);
                } else if (isAtLeftOpeningBorder(particle)) {
                    double y = 0;
                    p = new Particle(cantParticles + 4, new Position((W / 2) - (D / 2), y), 0, 0, 0, particle.getMass(), 0);
                    p.setWall(true);
                    result.add(p);
                } else if (isAtRightOpeningBorder(particle)) {
                    double y = 0;
                    p = new Particle(cantParticles + 5, new Position((W / 2) + (D / 2), y), 0, 0, 0, particle.getMass(), 0);
                    p.setWall(true);
                    result.add(p);
                }
            }
        } else {
            if (particle.getY() - particle.getRadius() < 0) {
                double y = -particle.getRadius();
                p = new Particle(cantParticles + 6, new Position(particle.getX(), y), 0, 0, particle.getRadius(), particle.getMass(), 0);
                p.setWall(true);
                result.add(p);
            }
        }
        return result;
    }

    private void addWallParticle(Set<Particle> result, Particle currentParticle, int cantParticles, double x, double y){
        Particle p;
        p = new Particle(cantParticles + 6, new Position(x, y), 0, 0, currentParticle.getRadius(), currentParticle.getMass(), 0);
        p.setWall(true);
        result.add(p);
    }

    private boolean isAtRightOpeningBorder(Particle particle) {
        double rightBorder = (W / 2) + (D / 2);
        return (particle.getX() + particle.getRadius() >= rightBorder) && (particle.getX() < rightBorder);
    }

    private boolean isAtLeftOpeningBorder(Particle particle) {
        double leftBorder = (W / 2) - (D / 2);
        return (particle.getX() - particle.getRadius() < leftBorder) && (particle.getX() > leftBorder);
    }

    private boolean isLeftOpeningFloor(Particle particle) {
        return particle.getX() >= ((W / 2) + (D / 2));
    }

    private boolean isRightOpeningFloor(Particle particle) {
        return particle.getX() <= ((W / 2) - (D / 2));
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

    /*private void logCaudalAndEnergy(double time,ArrayList<Integer>caudals,List<String> energyFileLog){
        if (Math.abs(time / delta2 - Math.round(time / delta2)) < deltaTime) {
            //System.out.println("Tiempo:"+time+"s, Caudal:"+caudal);
            caudals.add(caudal);
            double energy = computeKineticEnergy(particles);
            energyFileLog.add(Math.round(time * 1000.0) / 1000.0 + "\t" + energy + "\t" + (energy * particles.size()));
            caudal = 0;
        }
    }*/
}
