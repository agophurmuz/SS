import methods.Beeman;
import methods.CellIndexMethod;
import methods.ForceCalculation;
import models.Particle;

import java.util.List;

public class main {

    public static void main(String[] args) {

        double L = 0.5;
        double W = 0.3;
        boolean open = true;
        double D = 0.15;
        int framesToPrint = 200;
        double maxDiameter = 0.03;
        double minDiameter = 0.02;

        double particlesMass = 0.01;

        int M = 3;
        //double rc = 2 * 0.1;
        double rc = maxDiameter;
        double k = 1E5;
        //double gama = 20.0;
        double gama = 2 * Math.sqrt(k * particlesMass);
        double totalTime = 5.0;
        //double deltaTime = 1E-4;
        double deltaTime = 1E-5;
        //double deltaTime = 0.1 * Math.sqrt(particlesMass/k);
        //double delta2 = 0.02;
        double delta2 = 0.1;

        List<Particle> particles = ParticleGenerator.generateParticles(particlesMass, minDiameter, maxDiameter, L, W);

        CellIndexMethod method = new CellIndexMethod(false, M, L, rc, particles);
        Beeman beeman = new Beeman(deltaTime, L, W);

        Silo silo = new Silo(new ForceCalculation(k, gama, deltaTime), particles, method, beeman, totalTime, deltaTime, framesToPrint, open, L, W, D,
                particlesMass, minDiameter, maxDiameter);

        silo.runSilo();
    }
}
