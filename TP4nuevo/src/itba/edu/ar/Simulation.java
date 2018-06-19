package itba.edu.ar;

import itba.edu.ar.methods.Beeman;
import itba.edu.ar.methods.ForceCalculation;
import itba.edu.ar.models.Particle;
import itba.edu.ar.models.ParticleType;
import itba.edu.ar.output.FileGenerator;

import java.io.FileOutputStream;
import java.util.ArrayList;
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
            calculatePlanetsForces(planets);

            // calculate voyager forces
            calculateVoyagerForces(voyager);

            // Printeamos cada delta2
            printState(fileOutputStreamSim, time);

            // move planets next step
            List<Particle> nextStepPlanets = movePlanets(planets);

            // move voyager
            Particle nextStepVoyager = moveVoyager(voyager);

            // uddate state particles
            updateState(nextStepPlanets, nextStepVoyager);

            i++;
            time += deltaTime;
        }

        printMinDistances();
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


    private void calculateVoyagerForces(Particle voyager) {
        forceCalculation.setForces(voyager, planets);
    }

    private void calculatePlanetsForces(List<Particle> planets) {
        for (Particle p : planets) {
            forceCalculation.setForces(p, planets);
        }
    }

    private  List<Particle> movePlanets(List<Particle> planets) {
        List<Particle> nextStepPlanets = new ArrayList<>();
        for (Particle p : planets) {
            if(!p.getType().equals(ParticleType.SUN)) {
                Particle newPlanet = beeman.moveParticle(p);
                nextStepPlanets.add(newPlanet);
            } else {
                nextStepPlanets.add(p);
            }
        }
        return nextStepPlanets;
    }

    private Particle moveVoyager(Particle voyager) {
        return beeman.moveParticle(voyager);
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
