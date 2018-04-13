package itba.edu.ar;

import itba.edu.ar.methods.Beeman;
import itba.edu.ar.methods.Verlet;

import java.io.IOException;

public class App {

    public static void main(String[] args) throws IOException {

        double mass = 70;
        double k = Math.pow(10, 4);
        double gama = 100;
        double r0 = 1;
        double v0 = (-gama)/(2*mass);
        double totalTime = 5;
        double deltaTime = Math.pow(10, -4);

        //Beeman beeman = new Beeman(k, gama, r0, v0, mass, totalTime, deltaTime);

        //beeman.oscillate();

        Verlet verlet = new Verlet(k, gama, r0, v0, mass, totalTime, deltaTime);

        verlet.oscillate();
    }
}
