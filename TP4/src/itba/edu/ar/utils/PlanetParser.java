package itba.edu.ar.utils;


import itba.edu.ar.models.Position;
import itba.edu.ar.voyager.Body;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PlanetParser {

    public static final double EARTH_MASS = 5.9722E24;
    public static final double JUPITER_MASS = 1.89813E27;
    public static final double SATURN_MASS = 5.68319E26;

    public ArrayList<List<Body>> parseFile(String path,String fileBaseName,int filesInTimePeriod, int planetsInSystem) throws FileNotFoundException {

        ArrayList<List<Body>> year = new ArrayList<>();
        String fileExtension = ".tsv";
        double [] planetsMasses = {EARTH_MASS,JUPITER_MASS,SATURN_MASS};

        for(int j=0;j<filesInTimePeriod;j++){
            File periodFile = new File(path+fileBaseName+j+fileExtension);
            Scanner scanner = new Scanner(periodFile);
            List<Body> period = new ArrayList<>();
            for (int i = 0; i < planetsInSystem; i++) {

                double x = scanner.nextDouble();
                double y = scanner.nextDouble();
                double z = scanner.nextDouble();
                double vx = scanner.nextDouble();
                double vy = scanner.nextDouble();
                double vz = scanner.nextDouble();
                Position p = new Position(x,y);
                Body body = new Body(vx, vy,p,planetsMasses[i]);
                //DELETE
                System.out.println(x);
                System.out.println(y);
                System.out.println(vx);
                System.out.println(vy);
                System.out.println(planetsMasses[i]);
                period.add(body);
            }
            year.add(period);
        }

        return year;
    }
}
