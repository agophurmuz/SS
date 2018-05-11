import methods.Beeman;
import methods.BoundaryCondition;
import methods.CellIndexMethod;
import methods.Force;
import models.Particle;
import models.Position;

import java.io.FileOutputStream;
import java.util.*;

public class app {

    public static void main(String[] args) {

        double particlesMass = 0.01;
        double maxDiameter = 0.3;
        double minDiameter = 0.2;
        int cantParticles = 10;
        int L = 10;
        int W = 5;
        int D = 3;
        int M = 5;
        double rc = maxDiameter;
        double k = 1E4;
        double gama = 5.0;
        double totalTime = 5.0;
        double deltaTime = 1E-4;

        FileOutputStream fileOutputStream = FileGenerator.createOutputFilePoints("granular.xyz");
        //List<Particle> particles = ParticleGenerator.particlesGenerator(particlesMass, D/7, D/5, cantParticles, L, W);
        FileGenerator.addHeader(fileOutputStream, 2);
        /*for (Particle p : particles) {
            FileGenerator.addParticle(fileOutputStream, p);
        }
        FileGenerator.addWalls(fileOutputStream, particles.size(), particlesMass, L);*/
        //CellIndexMethod method = new CellIndexMethod(BoundaryCondition.NON_PERIODIC, M, L, rc, particles);
        Beeman beeman = new Beeman(new Force(k, gama, deltaTime), totalTime, deltaTime, L, W);
        double time = 0;
        Particle particle1 = new Particle(0, new Position(0.1,5), 0.02, 0, 0.1, particlesMass);
        Particle particle2 = new Particle(1, new Position(0.4,4.9), -0.02, 0, 0.1, particlesMass);
        FileGenerator.addParticle(fileOutputStream, particle1);
        FileGenerator.addParticle(fileOutputStream, particle2);
        int i = 0;
        while (time <= totalTime) {
            //TODO volver al iniicio en Y
            //List<Particle> nextParticles = new ArrayList<>();
            //Map<Particle, Set<Particle>> neighbors = method.getParticleNeighbors();
            //TODO chocar contra paredes
            //method.addWallParticleContact(neighbors, particles);

            /*for (Particle p : particles) {
                Particle nextP = beeman.moveParticle(p, neighbors.get(p));
                nextParticles.add(nextP);
                FileGenerator.addParticle(fileOutputStream, nextP);
            }*/

            if(i%100 == 0){
                FileGenerator.addHeader(fileOutputStream, 2);
                FileGenerator.addParticle(fileOutputStream, particle1);
                FileGenerator.addParticle(fileOutputStream, particle2);
            }
            i++;
            Set<Particle> set1 = new HashSet<>();
            set1.add(particle2);
            Set<Particle> set2 = new HashSet<>();
            set2.add(particle1);
            particle1 = beeman.moveParticle(particle1, set1);
            particle2 = beeman.moveParticle(particle2, set2);


            //FileGenerator.addWalls(fileOutputStream, particles.size(), particlesMass, L);

            //particles = nextParticles;
            //method.resetParticles(nextParticles);
            time += deltaTime;
        }
        //FileGenerator.addCorners(fileOutputStream, particles.size(), L);
        System.out.println(time);
    }
}
