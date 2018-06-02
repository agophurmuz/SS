package itba.edu;

import itba.edu.methods.Beeman;
import itba.edu.methods.CellIndexMethod;
import itba.edu.methods.ForceCalculation;
import itba.edu.models.Particle;
import itba.edu.models.Position;
import itba.edu.output.FileGenerator;

import java.io.FileOutputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Simulation {

    private List<Particle> particles;
    private CellIndexMethod cellIndexMethod;
    private Beeman beeman;
    private double deltaTime;
    private double delta2;
    private int exitedParticules = 0;
    private double window = 0.1;
    private boolean open;
    private double L;
    private double W;
    private double D;
    private double maxSpeed = 0;
    private double maxDiameter;
    private ForceCalculation forceCalculation;
    private List<String> particlesExitTimes;

    public Simulation(ForceCalculation forceCalculation, List<Particle> particles, CellIndexMethod cellIndexMethod, Beeman beeman,
                      double deltaTime, boolean open, double L, double W, double D, double particlesMass,
                      double minDiameter, double maxDiameter, double delta2) {

        this.particles = particles;
        this.cellIndexMethod = cellIndexMethod;
        this.beeman = beeman;
        this.deltaTime = deltaTime;
        this.open = open;
        this.L = L;
        this.W = W;
        this.D = D;
        this.maxDiameter = maxDiameter;
        this.delta2 = delta2;
        this.forceCalculation = forceCalculation;
    }

    public void runSimulation() {
        double simulationTime = 0;
        int i = 0;
        FileOutputStream fileOutputStream = FileGenerator.createOutputFilePoints("granular.xyz");
        particlesExitTimes = new ArrayList<>();

        while (!particles.isEmpty()) {

            removeExitedParticles(simulationTime);

            List<Particle> nextParticles = new ArrayList<>();

            // Calculamos vecinos para cada particula.
            Map<Particle, Set<Particle>> neighbors = cellIndexMethod.getParticleNeighbors(particles);

            // Agregamos particulas ficticias en caso de contacto con paredes y bordes.
            addWallParticleContact(neighbors);

            // Calculamos las fuerzas a las que están sometidas las particulas.
            calculateParticlesForces(neighbors);

            // Printeamos cada delta2
            printState(fileOutputStream,simulationTime);

            //Movemos las partículas al siguiente estado.
            moveParticles(neighbors, nextParticles);

            //Calcular la máxima velocidad de una partícula en la corrida y la asigna si es mayor a la mayor hasta ahora
            getSimulationMaxSpeed();

            //Actualizamos particulas con los nuevos estados.
            updateParticesState(nextParticles);

            i++;
            simulationTime += deltaTime;
        }

        System.out.println("Max speed:"+maxSpeed);
        System.out.println("Tiempo de simulación: "+simulationTime);
    }

    private void getSimulationMaxSpeed() {
        for (Particle p : particles) {
            if (p.getSpeed() > maxSpeed) {
                maxSpeed = p.getSpeed();
            }
        }
    }

    private void removeExitedParticles(double time) {
        List <Particle> particlesInRoom = new ArrayList<>();
        for (Particle p : particles) {
            if (!isOutOfRoom(p)) {
                particlesInRoom.add(p);
            }else{
                particlesExitTimes.add(((Double)time).toString());
                System.out.println(time);
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

    private void printState(FileOutputStream fileOutputStream,double time){
        if( time % delta2 < 1E-6){
            FileGenerator.addHeader(fileOutputStream, particles.size());
            for (Particle p: particles) {
                FileGenerator.addParticle(fileOutputStream, p);
            }
            FileGenerator.addWalls(fileOutputStream,particles.size(),L,W);
        }
    }

    private void calculateParticlesForces(Map<Particle, Set<Particle>> neighbors) {
        for (Particle p : particles) {
            forceCalculation.setForces(p, neighbors.get(p), getTarget(p));
        }
    }

    private Position<Double> getTarget(Particle p) {
        if(p.getX() >= 0 && p.getX()<= (W/2 - D/2) && p.getY()>0){
            return new Position(W/2 - D/2 + maxDiameter, maxDiameter);
        }
        if(p.getX() >= (W/2 + D/2) && p.getX() <= W && p.getY()>0) {
            return new Position(W/2 + D/2 - maxDiameter, maxDiameter);
        }
        return new Position(p.getX(), -L/10);
    }

    private boolean isOutOfRoom(Particle p) {
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
        p = new Particle(cantParticles + 7, new Position(x, y), 0, 0, currentParticle.getRadius(), currentParticle.getMass(), 0);
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

    public List<String> getParticlesExitTimes() {
        return particlesExitTimes;
    }
}
