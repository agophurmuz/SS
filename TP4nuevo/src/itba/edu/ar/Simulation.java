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


    public Simulation(double totalTime, double deltaTime, double delta2, List<Particle> planets, Beeman beeman,
                      Particle voyager, ForceCalculation forceCalculation, FileOutputStream fileOutputStreamMinDist,
                      FileOutputStream fileOutputStreamSim) {
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
            printState(fileOutputStreamSim, time);

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

        printMinDistances();
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

    private void printMinDistances() {
            FileGenerator.addDist(fileOutputStreamMinDist, jupiterDistance, saturnDistance);
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

    private void updateState(List<Particle> nextStepPlanets, Particle nextStepVoyager) {
        planets = nextStepPlanets;
        voyager = nextStepVoyager;
    }


    private void calculateVoyagerForces() {
        forceCalculation.setForces(voyager, planets);
    }

    private void calculatePlanetsForces() {
        for (Particle p : planets) {
            forceCalculation.setForces(p, planets);
        }
    }


    private void printState(FileOutputStream fileOutputStream, double time){
        //if( Math.abs(time / delta2 - Math.round(time / delta2)) < deltaTime){
        if(i % delta2 == 0) {
            FileGenerator.addHeader(fileOutputStream, planets.size() + 1);
            FileGenerator.addParticle(fileOutputStream, voyager);
            for (Particle p: planets) {
                FileGenerator.addParticle(fileOutputStream, p);
            }
        }
    }
}
