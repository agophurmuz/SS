package itba.edu;

import itba.edu.methods.Beeman;
import itba.edu.methods.CellIndexMethod;
import itba.edu.methods.ForceCalculation;
import itba.edu.models.Particle;
import itba.edu.models.Position;


import java.util.List;

public class main {

    public static void main(String[] args) {


        Config config = new Config();

        for (int c = 0; c < Config.N; c++) {

            List<Particle> particles = ParticleGenerator.generateParticles(config.getParticlesMass(), Config.minDiameter, Config.maxDiameter, Config.L, Config.W, Config.desiredV, 200);
            CellIndexMethod method = new CellIndexMethod(false, M, L, rc, particles);
            Beeman beeman = new Beeman(deltaTime);
            Room room = new Room(new ForceCalculation(k, gama, deltaTime, target, accelerationTime, A, B), particles, method, beeman, totalTime, deltaTime, framesToPrint, open, L, W, D,
                    particlesMass, minDiameter, maxDiameter, delta2);

            room.evacuate();

            float maxPreassure = 0;
            float minPreassure = Float.MAX_VALUE;

            for (Particle p : particles) {
                float particlePreasure = p.getSpeed();
                if (particlePreasure > maxPreassure) {
                    maxPreassure = particlePreasure;
                }

                if (particlePreasure < minPreassure) {
                    minPreassure = particlePreasure;
                }
            }

            System.out.println("Max Speed: " + maxPreassure);
            System.out.println("Min Speed: " + minPreassure);

        }
    }
}
