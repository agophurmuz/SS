package itba.edu.ar.utils;


import itba.edu.ar.models.Particle;
import itba.edu.ar.models.ParticleType;
import itba.edu.ar.models.Position;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PlanetParser {

    public static final double EARTH_MASS = 5.9722E24;
    public static final double JUPITER_MASS = 1.89813E27;
    public static final double SATURN_MASS = 5.68319E26;
    public static final double SUN_MASS = 1.988544E30;

    public List<Particle> parseFile(String path, String fileBaseName, int monthNumber, int planetsInSystem) throws FileNotFoundException {

        String fileExtension = ".tsv";
        double [] planetsMasses = {EARTH_MASS,JUPITER_MASS,SATURN_MASS};
        //Color[] planetColors = {new Color(24,42,97),new Color(135,121,111),new Color(227,224,192),new Color(2,2,2)};
        Color[] planetColors = {Color.red, Color.blue, Color.green};
        double[] planetRadious = {0.1,0.2,0.22};
        ParticleType[] bodyTypes = {ParticleType.EARTH, ParticleType.JUPITER, ParticleType.SATURN};

        File periodFile = new File(path+fileBaseName+monthNumber+fileExtension);
        Scanner scanner = new Scanner(periodFile);
        List<Particle> period = new ArrayList<>();
        period.add(new Particle(0, new Position(0, 0),0 ,0, SUN_MASS, ParticleType.SUN,0.4, Color.cyan));
        for (int i = 0; i < planetsInSystem; i++) {

            double x = scanner.nextDouble() * 1000;
            double y = scanner.nextDouble() * 1000;
            double z = scanner.nextDouble() * 1000;
            double vx = scanner.nextDouble() * 1000;
            double vy = scanner.nextDouble() * 1000;
            double vz = scanner.nextDouble() * 1000;
            Position p = new Position(x,y);
            Particle planet = new Particle(i + 1, p, vx, vy, planetsMasses[i], bodyTypes[i], planetRadious[i],planetColors[i]);
            //DELETE
            //System.out.println(x);
            //System.out.println(y);
            //System.out.println(vx);
            //System.out.println(vy);
            //System.out.println(planetsMasses[i]);
            //System.out.println(bodyTypes[i]);
            period.add(planet);
        }
        return period;
    }
}
