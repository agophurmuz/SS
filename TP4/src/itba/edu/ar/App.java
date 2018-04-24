package itba.edu.ar;

import itba.edu.ar.methods.Beeman;
import itba.edu.ar.methods.GearPredictorCorrector;
import itba.edu.ar.methods.Verlet;
import itba.edu.ar.models.Position;
import itba.edu.ar.utils.PlanetParser;
import itba.edu.ar.voyager.BeemanVoyager;
import itba.edu.ar.voyager.Body;
import itba.edu.ar.voyager.BodyType;


import java.io.IOException;

public class App {

    public static void main(String[] args) throws IOException {

        double mass = 70.0;
        double k = 1E4;
        double gama = 100.0;
        double r0 = 1.0;
        double v0 = (-gama)/(2*mass);
        double totalTime = 5.0;
        double deltaTime = 1E-4;//Math.pow(10, -4);

        //Beeman beeman = new Beeman(k, gama, r0, v0, mass, totalTime, deltaTime);

        //beeman.oscillate();

        //Verlet verlet = new Verlet(k, gama, r0, v0, mass, totalTime, deltaTime);

        //verlet.oscillate();

        GearPredictorCorrector gearPredictorCorrector = new GearPredictorCorrector(k, gama, r0, v0, mass, totalTime, deltaTime);
        gearPredictorCorrector.oscillate();

        //PlanetParser parser = new PlanetParser();
        //System.out.println(parser.parseFile("/Users/mminestrelli/Documents/ITBA/SS/TP4/src/itba/edu/ar/utils/data/year/","month-",1,3));
        //System.out.println(parser.parseFile("/Users/agophurmuz/Documents/ITBA/SS/TP4/src/itba/edu/ar/utils/data/year/","month-",12,3));

        //BeemanVoyager voyager = new BeemanVoyager(126144000, 86400, new Body(0,0,new Position(0,0), 721, BodyType.VOYAGER),
                //parser.parseFile("/Users/agophurmuz/Documents/ITBA/SS/TP4/src/itba/edu/ar/utils/data/year/","month-",1,3));
        //voyager.move();
    }
}
