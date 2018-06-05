package itba.edu;

import itba.edu.methods.Beeman;
import itba.edu.methods.CellIndexMethod;
import itba.edu.methods.ForceCalculation;
import itba.edu.models.Particle;
import itba.edu.output.PrintUtils;
import java.sql.Timestamp;
import java.util.ArrayList;
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
    public static double gama = 2 * Math.sqrt(k * particlesMass);
    public static double deltaTime = 1E-4;
    public static double delta2 = 0.1;
    public static double accelerationTime = 0.5;
    public static double A = 2000;
    public static double B = 0.08;
    double desiredV = 1.5;

    public static void main(String[] args) {

        double startTime = System.currentTimeMillis() / 1000;

        runSimulations(1,1.0,2.0,0.1);

        System.out.println("Tiempo de corrida: "+((System.currentTimeMillis() / 1000)-startTime)/60);
    }

    /***
     * Run one batch of #amountOfSimulations simulations for an specified desired v
     * @param amountOfSimulations
     * @param desiredV
     */
    private static void runSimulations(int amountOfSimulations, double desiredV){
        List<Particle> particles = ParticleGenerator.generateParticles(particlesMass, minDiameter, maxDiameter, L, W, desiredV, 100);
        CellIndexMethod method = new CellIndexMethod(false, M, L, rc, particles);
        Beeman beeman = new Beeman(deltaTime);
        List<String> lastExitedPerson = new ArrayList<>();
        String batchIdentifier = "sim-"+ new Timestamp(System.currentTimeMillis());
        PrintUtils.createFolder(batchIdentifier);

        Simulation currentSimulation;
        for(int i=0;i<amountOfSimulations;i++){
            currentSimulation = new Simulation(new ForceCalculation(k, gama, deltaTime, accelerationTime, A, B), particles, method, beeman, deltaTime, open, L, W, D,
                    particlesMass, minDiameter, maxDiameter,delta2,"./outputFiles/"+batchIdentifier + "/simulacion-"+i+"-v-"+Math.round(desiredV)+".xyz", desiredV);
            currentSimulation.runSimulation();
            PrintUtils.writeLogFileFromList(currentSimulation.getParticlesExitTimes(),"Descarga-Simulacion"+i+".tsv", batchIdentifier);
            lastExitedPerson.add(currentSimulation.getLastExitedPersonTime());
        }
        PrintUtils.writeLogFileFromList(lastExitedPerson,"Tiempos-de-salidas-ultima-persona-"+Math.round(desiredV)+".tsv",batchIdentifier);
    }

    /***
     * Run various batches of #amountOfSimulations simulations for many v's from startDesiredV to endDesiredV in steps of step
     * @param amountOfSimulationsPerBatch
     * @param startDesiredV
     * @param endDesiredV
     * @param step
     */
    private static void runSimulations(int amountOfSimulationsPerBatch,double startDesiredV, double endDesiredV,double step){
        for(double v= startDesiredV; v<endDesiredV;v+=step){
            runSimulations(amountOfSimulationsPerBatch,v);
        }
    }

}
