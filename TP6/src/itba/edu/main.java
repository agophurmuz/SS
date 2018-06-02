package itba.edu;

import itba.edu.methods.Beeman;
import itba.edu.methods.CellIndexMethod;
import itba.edu.methods.ForceCalculation;
import itba.edu.models.Particle;
import itba.edu.output.PrintUtils;


import java.util.List;

public class main {
    public static double L = 20;
    public static double W = 20;
    public static boolean open = true;
    public static double D = 1.2;
    public static double maxDiameter = 0.7;
    public static double minDiameter = 0.5;
    public static double particlesMass = 80.0;
    public static int M = 15;
    public static double rc = maxDiameter;
    public static double k = 1E5;
    public static double gama = 0.001 * 2 * Math.sqrt(k * particlesMass);
    public static double deltaTime = 1E-6;
    public static double delta2 = 0.1;
    public static double accelerationTime = 0.5;
    public static double A = 2000;
    public static double B = 0.08;
    double desiredV = 1.5;

    public static void main(String[] args) {

        double startTime = System.currentTimeMillis() / 1000;


/*        List<Particle> particles = ParticleGenerator.generateParticles(particlesMass, minDiameter, maxDiameter, L, W, desiredV, 50);

        CellIndexMethod method = new CellIndexMethod(false, M, L, rc, particles);
        Beeman beeman = new Beeman(deltaTime);
        Simulation simulation = new Simulation(new ForceCalculation(k, gama, deltaTime, accelerationTime, A, B), particles, method, beeman, totalTime, deltaTime, open, L, W, D,
                particlesMass, minDiameter, maxDiameter,delta2);

        simulation.runSimulation();*/
        runSimulations(2);
        System.out.println("Tiempo de corrida: "+((System.currentTimeMillis() / 1000)-startTime)/60);
    }

    private static void runSimulations(int amountOfSimulations){
        double desiredV = 1.5;
        List<Particle> particles = ParticleGenerator.generateParticles(particlesMass, minDiameter, maxDiameter, L, W, desiredV, 5);
        CellIndexMethod method = new CellIndexMethod(false, M, L, rc, particles);
        Beeman beeman = new Beeman(deltaTime);

        Simulation currentSimulation;
        for(int i=0;i<amountOfSimulations;i++){
            currentSimulation = new Simulation(new ForceCalculation(k, gama, deltaTime, accelerationTime, A, B), particles, method, beeman, deltaTime, open, L, W, D,
                    particlesMass, minDiameter, maxDiameter,delta2);
            currentSimulation.runSimulation();
            PrintUtils.writeLogFileFromList(currentSimulation.getParticlesExitTimes(),"./outputFiles/Descarga-Simulacion"+i+".tsv");
        }
    }
}
