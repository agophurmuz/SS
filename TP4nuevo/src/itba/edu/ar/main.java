package itba.edu.ar;

import itba.edu.ar.methods.Beeman;
import itba.edu.ar.methods.ForceCalculation;
import itba.edu.ar.models.Particle;
import itba.edu.ar.models.ParticleType;
import itba.edu.ar.models.Position;
import itba.edu.ar.output.FileGenerator;
import itba.edu.ar.utils.PlanetParser;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

public class main {

    public static double G = 6.693E-11;
    public static double deltaTime2 = 6000;
    public static double deltaTime = 30;
    public static double totalTime = 126144000;
    public static double voyagerMass = 721;

    private static final double VOYAGER_DISTANCE = 1500000; /* 1500 km */
    private static final double VOYAGER_SPEED = 14000; /* 11 km/s */
    private static final double EARTH_RADIUS = 6371000;
    private static final int PLANETS_IN_SYSTEM = 3;
    private static final String DAY = "day";
    private static final String MONTH = "month";
    private static final String WEEK = "week";
    private static final String HOUR = "hour";


    public static void main(String[] args) throws FileNotFoundException{
        setUpSimulationPeriodAndRun(HOUR);
    }

    private static int getFilesInFolder(String folder){
        int count = 0;
        try (Stream<Path> files = Files.list(Paths.get(folder))) {
            count = (int)files.count();
        }catch (Exception e){

        }
        return count;
    }

    private static void  setUpSimulationPeriodAndRun(String simulationPeriod) throws FileNotFoundException{

        FileOutputStream fileOutputStreamMinDist = FileGenerator.createOutputFile("minDist.tsv");
        FileOutputStream fileOutputStreamVelocities = FileGenerator.createOutputFile("voyagerVelocities.tsv");
        FileGenerator.addDistHeader(fileOutputStreamMinDist);
        FileGenerator.addVoyagerVelocityHeader(fileOutputStreamVelocities);
        String basePath = new File("").getAbsolutePath() + "/TP4nuevo/src/itba/edu/ar";
        String localPath = "/utils/data/"+simulationPeriod+"/";
        String fileBaseName = simulationPeriod+"-";
        int filesInFolder = getFilesInFolder(basePath+localPath);

        for (int i = 0; i < filesInFolder; i++) {
            ForceCalculation forceCalculation = new ForceCalculation(G);
            Beeman beeman = new Beeman(deltaTime);
            PlanetParser parser = new PlanetParser();
            List<Particle> planets = parser.parseFile(basePath +localPath,
                    fileBaseName, i, PLANETS_IN_SYSTEM);
            Particle earth = null;
            Particle sun = null;
            for (Particle p : planets) {
                if (p.getType().equals(ParticleType.EARTH)) {
                    earth = p;
                }
                if (p.getType().equals(ParticleType.SUN)) {
                    sun = p;
                }

            }
            double normalXvector = forceCalculation.getNormalXVector(sun, earth);
            double normalYvector = forceCalculation.getNormalYVector(sun, earth);
            double deltaX = VOYAGER_DISTANCE * normalXvector;
            double deltaY = VOYAGER_DISTANCE * normalYvector;
            double vxVoyager = VOYAGER_SPEED * (-normalYvector);
            double vyVoyager = VOYAGER_SPEED * normalXvector;
            Position<Double> newPosition = new Position<>(earth.getX() + EARTH_RADIUS + deltaX, earth.getY() + EARTH_RADIUS + deltaY);
            Particle voyager = new Particle(planets.size(), newPosition, earth.getVx() + vxVoyager, earth.getVy() + vyVoyager,
                    voyagerMass, ParticleType.VOYAGER,0.1, Color.gray);
            FileOutputStream fileOutputStreamSim = FileGenerator.createOutputFile(simulationPeriod +"-"+ i +"Simulation.xyz");
            Simulation simulation = new Simulation(totalTime, deltaTime, deltaTime2, planets, beeman, voyager, forceCalculation,
                    fileOutputStreamMinDist, fileOutputStreamSim, fileOutputStreamVelocities);
            simulation.run();
        }
    }
}
