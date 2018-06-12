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


    public Simulation(double totalTime, double deltaTime, double delta2, List<Particle> planets, Beeman beeman,
                      Particle voyager, ForceCalculation forceCalculation) {
        this.totalTime = totalTime;
        this.deltaTime = deltaTime;
        this.delta2 = delta2;
        this.planets = planets;
        this.beeman = beeman;
        this.voyager = voyager;
        this.forceCalculation = forceCalculation;
    }


    public void run() {
        FileOutputStream fileOutputStream = FileGenerator.createOutputFile("simulationMonths.xyz");
        double time = 0.0;
        while(time < totalTime) {

            List<Particle> nextStepPlanets = new ArrayList<>();

            // calculate planet forces
            calculatePlanetsForces(planets);

            // Printeamos cada delta2
            printState(fileOutputStream, time);

            // move planets next step
            move(planets, nextStepPlanets);


            planets = nextStepPlanets;
            time += deltaTime;
        }
    }

    private void calculatePlanetsForces(List<Particle> planets) {
        for (Particle p : planets) {
            forceCalculation.setForces(p, planets);
        }
    }

    private void move(List<Particle> planets, List<Particle> nextStepPlanets) {
        for (Particle p : planets) {
            if(!p.getType().equals(ParticleType.SUN)) {
                Particle newPlanet = beeman.movePlanet(p);
                nextStepPlanets.add(newPlanet);
            } else {
                nextStepPlanets.add(p);
            }
        }
    }

    private void printState(FileOutputStream fileOutputStream, double time){
        if( Math.abs(time / delta2 - Math.round(time / delta2)) < deltaTime){
            FileGenerator.addHeader(fileOutputStream, planets.size());
            //FileGenerator.addParticle(fileOutputStream, voyager);
            for (Particle p: planets) {
                FileGenerator.addParticle(fileOutputStream, p);
            }
        }
    }
}
