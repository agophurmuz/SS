package itba.edu.ar;

import itba.edu.ar.methods.BoundaryCondition;
import itba.edu.ar.methods.CellIndexMethod;
import itba.edu.ar.models.Particle;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by marlanti on 3/6/18.
 */
public class app {

    public static void main(String[] args) {

        int N = 5;
        int M = 3;
        double rc = 8;
        int L = 30;
        double radius = 0.25;

        List<Particle> particles = ParticleSystemGenerator.generateRandomParticleSystem(N, L, radius);


        long startTime = System.currentTimeMillis();

        CellIndexMethod cellIndexMethod = new CellIndexMethod(BoundaryCondition.PERIODIC, M, L, rc, particles);

        HashMap<Particle, Set<Particle>> result = cellIndexMethod.getParticleNeighbors();

        FileGenerator.createOutputFile("OutputFile.txt", result, 8);

        long endTime = System.currentTimeMillis();

        long time = endTime - startTime;

        System.out.println("M = " + M + " - tiempo: " + time);

        System.out.println(result);

        System.out.println(particles);

    }

}
