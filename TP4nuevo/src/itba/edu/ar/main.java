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
    public static double deltaTime2 = 6000;
    public static double deltaTime = 30;
    public static double totalTime = 126144000;
    public static double voyagerMass = 721;

    private static final double VOYAGER_DISTANCE = 1500000; /* 1500 km */
    private static final double VOYAGER_SPEED = 13000; /* 11 km/s */
    private static final double EARTH_RADIUS = 6371000;

    public static void main(String[] args) throws FileNotFoundException {
        Beeman beeman = new Beeman(deltaTime);
        PlanetParser parser = new PlanetParser();
        List<Particle> planets = parser.parseFile("/Users/agophurmuz/Documents/ITBA/SS/TP4nuevo/src/itba/edu/ar/utils/data/year/",
                "month-",8,3);
        ForceCalculation forceCalculation = new ForceCalculation(G);
        Particle earth = null;
        Particle sun = null;
        for (Particle p : planets) {
            if(p.getType().equals(ParticleType.EARTH)){
                earth = p;
            }
            if(p.getType().equals(ParticleType.SUN)){
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
                voyagerMass, ParticleType.VOYAGER);
        Simulation simulation = new Simulation(totalTime, deltaTime, deltaTime2, planets, beeman, voyager, forceCalculation);
        simulation.run();
    }
}
