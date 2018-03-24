package itba.edu.ar;

import itba.edu.ar.methods.BrownianMovement;
import itba.edu.ar.models.Particle;
import itba.edu.ar.models.ParticleGenerator;

import java.io.FileOutputStream;
import java.util.List;

public class app {

    public static void main(String[] args) {
        int cantRun = 10;
        double L = 0.55;
        List<Particle> particles = ParticleGenerator.particlesGenerator(100, 0.1, 0.05, 0.005, 10, L,0.1);
        BrownianMovement brownianMovement = new BrownianMovement(0.5, particles);
        FileOutputStream fileOutputStream = FileGenerator.createOutputFilePoints("OutputTP3.xyz");

        for (int i = 0; i < cantRun; i++) {
            for (Particle particle : particles) {
                brownianMovement.timeCrashParticleToWall(particle);
                for (Particle particle1 : particles) {
                    brownianMovement.timeCrashParticleToParticle(particle, particle1);
                }
            }
            brownianMovement.move();
            brownianMovement.crash();
            FileGenerator.addPointsToFile(fileOutputStream, particles);
        }
    }
}
