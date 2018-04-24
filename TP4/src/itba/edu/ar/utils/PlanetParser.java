package itba.edu.ar.utils;


import itba.edu.ar.models.Position;
import itba.edu.ar.voyager.Body;
import itba.edu.ar.voyager.BodyType;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PlanetParser {

    public static final double EARTH_MASS = 5.9722E24;
    public static final double JUPITER_MASS = 1.89813E27;
    public static final double SATURN_MASS = 5.68319E26;
    private static final double SUN_MASS = 1.988544E30;

    public ArrayList<List<Body>> parseFile(String path,String fileBaseName,int filesInTimePeriod, int planetsInSystem) throws FileNotFoundException {

        ArrayList<List<Body>> year = new ArrayList<>();
        String fileExtension = ".tsv";
        double [] planetsMasses = {EARTH_MASS,JUPITER_MASS,SATURN_MASS};
        BodyType[] bodyTypes = {BodyType.EARTH, BodyType.JUPITER, BodyType.SATURN};

        for(int j=0;j<filesInTimePeriod;j++){
            File periodFile = new File(path+fileBaseName+j+fileExtension);
            Scanner scanner = new Scanner(periodFile);
            List<Body> period = new ArrayList<>();
            period.add(new Body(0,0, new Position(0, 0), SUN_MASS, BodyType.SUN));
            for (int i = 0; i < planetsInSystem; i++) {

                double x = scanner.nextDouble() * 1000;
                double y = scanner.nextDouble() * 1000;
                double z = scanner.nextDouble() * 1000;
                double vx = scanner.nextDouble() * 1000;
                double vy = scanner.nextDouble() * 1000;
                double vz = scanner.nextDouble() * 1000;
                Position p = new Position(x,y);
                Body body = new Body(vx, vy, p, planetsMasses[i], bodyTypes[i]);
                //DELETE
                //System.out.println(x);
                //System.out.println(y);
                //System.out.println(vx);
                //System.out.println(vy);
                //System.out.println(planetsMasses[i]);
                //System.out.println(bodyTypes[i]);
                period.add(body);
            }
            year.add(period);
        }

        return year;
    }
}
