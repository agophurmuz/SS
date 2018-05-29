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
        double gama = 2 * Math.sqrt(k * particlesMass);
        double totalTime = 10.0;
        double deltaTime = 1E-5;
        double delta2 = 0.001;
        double accelerationTime = 0.5;
        double A = 2000;
        double B = 0.08;
        double desiredV = 6.0;
        Particle target = new Particle(1000, new Position<>(W/2, -L/10), 0.0, 0.0, 0.0, 0.0, 0);

        List<Particle> particles = ParticleGenerator.generateParticles(particlesMass, minDiameter, maxDiameter, L, W, desiredV, 200);

        CellIndexMethod method = new CellIndexMethod(false, M, L, rc, particles);
        Beeman beeman = new Beeman(deltaTime);
        Silo silo = new Silo(new ForceCalculation(k, gama, deltaTime, target, accelerationTime, A, B), particles, method, beeman, totalTime, deltaTime, framesToPrint, open, L, W, D,
                particlesMass, minDiameter, maxDiameter,delta2);

        silo.runSilo();

        float maxPreassure = 0;
        float minPreassure = Float.MAX_VALUE;

        for (Particle p: particles) {
            float particlePreasure = p.getSpeed();
            if(particlePreasure > maxPreassure) {
                maxPreassure = particlePreasure;
            }

            if(particlePreasure < minPreassure){
                minPreassure = particlePreasure;
            }
        }

        System.out.println("Max Speed: "+maxPreassure);
        System.out.println("Min Speed: "+minPreassure);
    }
}
