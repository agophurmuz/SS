package itba.edu.ar;

import itba.edu.ar.methods.Beeman;
import itba.edu.ar.methods.ForceCalculation;
import itba.edu.ar.models.Particle;
import itba.edu.ar.models.ParticleType;
import itba.edu.ar.output.FileGenerator;

import java.io.FileOutputStream;
import java.util.List;

public class Simulation {

    private double totalTime;
    private double deltaTime;
    private double delta2;
    private List<Particle> planets;
    private Beeman beeman;
    private Particle voyager;
    private ForceCalculation forceCalculation;
    private int i;
    private double jupiterDistance = Double.MAX_VALUE;
    private double saturnDistance = Double.MAX_VALUE;
    private FileOutputStream fileOutputStreamMinDist;
    private FileOutputStream fileOutputStreamSim;
    private FileOutputStream fileOutputStreamVelocities;


    public Simulation(double totalTime, double deltaTime, double delta2, List<Particle> planets, Beeman beeman,
                      Particle voyager, ForceCalculation forceCalculation, FileOutputStream fileOutputStreamMinDist,
                      FileOutputStream fileOutputStreamSim, FileOutputStream fileOutputStreamVelocities) {
        this.totalTime = totalTime;
        this.deltaTime = deltaTime;
        this.delta2 = delta2;
        this.planets = planets;
        this.beeman = beeman;
        this.voyager = voyager;
        this.forceCalculation = forceCalculation;
        this.i = 0;
        this.fileOutputStreamMinDist = fileOutputStreamMinDist;
        this.fileOutputStreamSim = fileOutputStreamSim;
        this.fileOutputStreamVelocities = fileOutputStreamVelocities;
    }


    public void run() {
        double time = 0.0;
        while(time < totalTime) {

            //calculate voyager min dist to saturn
            calculateMinDistToVoyager();

            // calculate planet forces
            calculatePlanetsForces();

            // calculate voyager forces
            calculateVoyagerForces();

            // Printeamos cada delta2
            printState(time);

            //calculo las posiciones nuevas
            calculateNewPositions();

            //calculo las nuevas velocidades
            calculateNewVelocities();

            // calculo la nueva posicion de voyager
            calculateNewPositionsVoyager();

            // calculo la nueva velocidad de voyager
            calculateNewVelocitiesVoyager();


            i++;
            time += deltaTime;
        }

        printMinDistances(time);
    }

    private void printVoyagerVelocities(double time) {
        double voyagerVelocityAbs = Math.sqrt(Math.pow(voyager.getVx(),2) + Math.pow(voyager.getVy(),2));
        FileGenerator.addVoyagerVelocity(fileOutputStreamVelocities, time, voyagerVelocityAbs);
    }

    private void calculateNewVelocitiesVoyager() {
        double[] newForces = forceCalculation.calculateForce(voyager, planets);
        beeman.calculateVelocity(voyager, (newForces[0]/voyager.getMass()), (newForces[1]/voyager.getMass()));
    }

    private void calculateNewPositionsVoyager() {
        beeman.calculatePosition(voyager);
    }

    private void calculateNewVelocities() {
        for (Particle p : planets) {
            if (!p.getType().equals(ParticleType.SUN)) {
                double[] newForces = forceCalculation.calculateForce(p, planets);
                beeman.calculateVelocity(p, (newForces[0]/p.getMass()), (newForces[1]/p.getMass()));
            }
        }
    }

    private void calculateNewPositions() {
        for (Particle p : planets) {
            if (!p.getType().equals(ParticleType.SUN)) {
                beeman.calculatePosition(p);
            }
        }
    }

    private void printMinDistances(double time) {
        FileGenerator.addDist(fileOutputStreamMinDist, jupiterDistance, saturnDistance, time);
    }


    private void calculateMinDistToVoyager() {

        for (Particle p : planets) {
            if(p.getType().equals(ParticleType.JUPITER)){
                double aux = forceCalculation.getDistance(voyager, p);
                if(aux < jupiterDistance) {
                    jupiterDistance = aux;
                }
            }
            if(p.getType().equals(ParticleType.SATURN)){
                double aux = forceCalculation.getDistance(voyager, p);
                if(aux < saturnDistance) {
                    saturnDistance = aux;
                }
            }
        }
    }

    private void calculateVoyagerForces() {
        forceCalculation.setForces(voyager, planets);
    }

    private void calculatePlanetsForces() {
        for (Particle p : planets) {
            forceCalculation.setForces(p, planets);
        }
    }


    private void printState(double time){
        if(i % delta2 == 0) {
            // Printeamos el modulo de la velocidad de la sonda en función del tiempo.
            printVoyagerVelocities(time);
            FileGenerator.addHeader(fileOutputStreamSim, planets.size() + 1);
            FileGenerator.addParticle(fileOutputStreamSim, voyager);
            for (Particle p: planets) {
                FileGenerator.addParticle(fileOutputStreamSim, p);
            }
        }
    }
}
