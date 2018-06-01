package itba.edu;

import itba.edu.methods.Beeman;
import itba.edu.methods.CellIndexMethod;
import itba.edu.methods.ForceCalculation;
import itba.edu.models.Particle;
import itba.edu.models.Position;


import java.util.List;

public class main {

    public static void main(String[] args) {

        double L = 20;
        double W = 20;
        boolean open = true;
        double D = 1.2;
        int framesToPrint = 200;
        double maxDiameter = 0.7;
        double minDiameter = 0.5;
        double particlesMass = 80.0;
        int M = 15;
        double rc = maxDiameter;
        double k = 1E5;
        double gama = 10 * 2 * Math.sqrt(k * particlesMass);
        double totalTime = 5.0;
        double deltaTime = 1E-6;
        double delta2 = 0.1;
        double accelerationTime = 0.5;
        double A = 2000;
        double B = 0.08;
        double desiredV = 6.0;
        double startTime = System.currentTimeMillis() / 1000;


        List<Particle> particles = ParticleGenerator.generateParticles(particlesMass, minDiameter, maxDiameter, L, W, desiredV, 50);

        CellIndexMethod method = new CellIndexMethod(false, M, L, rc, particles);
        Beeman beeman = new Beeman(deltaTime);
        Silo silo = new Silo(new ForceCalculation(k, gama, deltaTime, accelerationTime, A, B), particles, method, beeman, totalTime, deltaTime, framesToPrint, open, L, W, D,
                particlesMass, minDiameter, maxDiameter,delta2);

        silo.runSilo();
        System.out.println("Tiempo de corrida: "+((System.currentTimeMillis() / 1000)-startTime)/60);
    }
}
