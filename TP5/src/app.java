import methods.Beeman;
import methods.BoundaryCondition;
import methods.CellIndexMethod;
import methods.Force;
import models.Particle;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class app {

    public static void main(String[] args) {

        double particlesMass = 0.01;
        double maxDiameter = 0.3;
        double minDiameter = 0.2;
        int cantParticles = 10;
        int L = 10;
        int M = 5;
        double rc = maxDiameter;
        double k = 1E4;
        double gama = 100.0;
        double totalTime = 5.0;
        double deltaTime = 1E-4;

        FileOutputStream fileOutputStream = FileGenerator.createOutputFilePoints("granular.xyz");
        List<Particle> particles = ParticleGenerator.particlesGenerator(particlesMass, minDiameter, maxDiameter, cantParticles, L);
        for (Particle p : particles) {
            FileGenerator.addParticle(fileOutputStream, p);
        }
        CellIndexMethod method = new CellIndexMethod(BoundaryCondition.NON_PERIODIC, M, L, rc, particles);
        Beeman beeman = new Beeman(new Force(k, gama), totalTime, deltaTime);
        double time = 0;
        while (time <= totalTime) {
            //TODO volver al iniicio en Y
            List<Particle> nextParticles = new ArrayList<>();
            Map<Particle, Set<Particle>> neighbors = method.getParticleNeighbors();
            //TODO chocar contra paredes
            FileGenerator.addHeader(fileOutputStream, particles.size());
            for (Particle p : particles) {
                Particle nextP = beeman.moveParticle(p, neighbors.get(p));
                nextParticles.add(nextP);
                FileGenerator.addParticle(fileOutputStream, nextP);
            }

            particles = nextParticles;
            method.resetParticles(nextParticles);
            time += deltaTime;
        }
        FileGenerator.addCorners(fileOutputStream, particles.size(), L);
        System.out.println(time);
    }
}
