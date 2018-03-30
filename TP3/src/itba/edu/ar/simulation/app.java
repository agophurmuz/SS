package itba.edu.ar.simulation;

import itba.edu.ar.simulation.methods.BrownianMovement;
import itba.edu.ar.simulation.models.Particle;
import itba.edu.ar.simulation.models.ParticleGenerator;

import java.io.FileOutputStream;
import java.util.List;

public class app {

    public static void main(String[] args) {
        int cantSimulations = 10;

        for (int s = 0; s < cantSimulations; s++) {
            int cantRun = 6000;
            double L = 0.5;
            double v = 0.1;
            double bigParticleInitX = L/2;
            double bigParticleInitY = L/2;
            double bigMassParticle = 100;
            List<Particle> particles = ParticleGenerator.particlesGenerator(bigMassParticle, 0.1, 0.05, 0.005, 100, L, v, bigParticleInitX, bigParticleInitY);
            BrownianMovement brownianMovement = new BrownianMovement(0.5, particles);
            FileOutputStream fileOutputStream = FileGenerator.createOutputFilePoints("OutputTP3-simulation-" + s + ".xyz");
            FileOutputStream fileBigParticleOutputStream = FileGenerator.createOutputFilePoints("BigParticlePath-simulation-" + s + ".csv");
            FileOutputStream fileBigParticleTimeOutputStream = FileGenerator.createOutputFilePoints("BigParticleEventTime-simulation-" + s + ".csv");
            double time = 0;
            double aux;
            for (int i = 0; i < cantRun; i++) {
                for (Particle particle : particles) {
                    brownianMovement.timeCrashParticleToWall(particle);
                    for (Particle particle1 : particles) {
                        if (!particle.equals(particle1))
                            brownianMovement.timeCrashParticleToParticle(particle, particle1);
                    }
                }
                brownianMovement.move();
                aux = brownianMovement.getMinCrash().getTime();
                System.out.println("Numero de RUN: " + i + "****************************");
                System.out.println("Final minCrash: " + aux);
                System.out.println("****************************************************");
                brownianMovement.crash();
                FileGenerator.addPointsToFile(fileOutputStream, particles);
                FileGenerator.addBigParticlePath(fileBigParticleOutputStream, particles.get(0), bigParticleInitX, bigParticleInitY, v, bigMassParticle);
                time += aux;
                FileGenerator.addBigParticleEventTime(fileBigParticleTimeOutputStream, particles.get(0), bigParticleInitX, bigParticleInitY, v, time);
            }
        }

    }
}
