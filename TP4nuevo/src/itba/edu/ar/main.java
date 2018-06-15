package itba.edu.ar;

import itba.edu.ar.methods.Beeman;
import itba.edu.ar.methods.ForceCalculation;
import itba.edu.ar.models.Particle;
import itba.edu.ar.models.ParticleType;
import itba.edu.ar.models.Position;
import itba.edu.ar.utils.PlanetParser;

import java.io.FileNotFoundException;
import java.util.List;

public class main {

    public static double G = 6.693E-11;
    public static double deltaTime2 = 600000;
    public static double deltaTime = 60;
    public static double totalTime = 126144000;
    public static double voyagerMass = 721;

    public static void main(String[] args) throws FileNotFoundException {
        Beeman beeman = new Beeman(deltaTime);
        PlanetParser parser = new PlanetParser();
        List<Particle> planets = parser.parseFile("/Users/agophurmuz/Documents/ITBA/SS/TP4nuevo/src/itba/edu/ar/utils/data/year/","month-",3,3);
        Particle voyager = new Particle(planets.size(), new Position(0.0,0.0), 0, 0, voyagerMass, ParticleType.VOYAGER);
        ForceCalculation forceCalculation = new ForceCalculation(G);
        Simulation simulation = new Simulation(totalTime, deltaTime, deltaTime2, planets, beeman, voyager, forceCalculation);
        simulation.run();
    }
}
