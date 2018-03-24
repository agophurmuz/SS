package itba.edu.ar;

import itba.edu.ar.methods.BoundaryCondition;
import itba.edu.ar.methods.CellIndexMethod;
import itba.edu.ar.models.Particle;

import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by marlanti on 3/6/18.
 */
public class app {

    public static void main(String[] args) {

        int N = 100;
        int M = 5;
        double rc = 1;
        int L = 10;
        double radius = 0;

        List<Particle> particles = ParticleSystemGenerator.generateRandomParticleSystem(N, L, radius);
        FileGenerator.particlesToFile("particles.xyz", particles);

        long startTime = System.currentTimeMillis();

        CellIndexMethod cellIndexMethod = new CellIndexMethod(BoundaryCondition.PERIODIC, M, L, rc, particles);
        HashMap<Particle, Set<Particle>> result = cellIndexMethod.getParticleNeighbors();


        FileGenerator.createOutputFileNeighbors("OutputFileNeighborsM2.xyz", result);

        FileOutputStream fileOutputStream = FileGenerator.createOutputFilePoints("OutputFilePointsM2.xyz");
        for(Particle p : particles){
            FileGenerator.addPointsToFile(fileOutputStream, result, p);
        }

        long endTime = System.currentTimeMillis();

        long time = endTime - startTime;

        System.out.println("M = " + M + " - tiempo: " + time);

        //System.out.println(result);

        //System.out.println(particles);


    }

}
