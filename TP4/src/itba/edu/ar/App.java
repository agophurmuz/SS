package itba.edu.ar;

import itba.edu.ar.methods.Beeman;
import itba.edu.ar.methods.BeemanNoPredicted;
import itba.edu.ar.methods.GearPredictorCorrector;
import itba.edu.ar.methods.Verlet;


import java.io.IOException;
import java.util.List;

public class App {

    public static void main(String[] args) throws IOException {

        double mass = 70.0;
        double k = 1E4;
        double gama = 100.0;
        double r0 = 1.0;
        double v0 = (-gama)/(2*mass);
        double totalTime = 5.0;
        double deltaTime = 1E-4;//Math.pow(10, -4);

        Beeman beeman = new Beeman(k, gama, r0, v0, mass, totalTime, deltaTime);

        beeman.oscillate();

        Verlet verlet = new Verlet(k, gama, r0, v0, mass, totalTime, deltaTime);

        verlet.oscillate();

        GearPredictorCorrector gearPredictorCorrector = new GearPredictorCorrector(k, gama, r0, v0, mass, totalTime, deltaTime);
        gearPredictorCorrector.oscillate();

    }
}
