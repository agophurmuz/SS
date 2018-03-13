package itba.edu.ar;

import itba.edu.ar.models.Cell;
import itba.edu.ar.models.Particle;
import itba.edu.ar.models.Position;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class App {

    public static void main(String[] args) throws IOException {

        int N = 5;
        int M = 3;
        double rc = 8;
        int L = 30;
        double speed = 0.3;
        double pi = Math.PI;
        double eta = 0.1;
        int cantRun = 100;
        double radius = 0;

        List<Particle> particles = ParticleSystemGenerator.generateRandomParticleWithSpeedSystem(N, L, radius, speed);

        //long startTime = System.currentTimeMillis();

        SelfMovingParticles selfMovingParticles = new SelfMovingParticles(L,rc, eta, M, speed);

        //Set<Particle> result = selfMovingParticles.move(board);

        //FileGenerator.createOutputFile("OutputFile.txt", result, 8);

        //long endTime = System.currentTimeMillis();

        //long time = endTime - startTime;

        //System.out.println("M = " + M + "tiempo: " + time);

        //System.out.println(result);

        //System.out.println(selfMovingParticles.polarization(particles));

        FileOutputStream fileOutputStream = FileGenerator.createOutputFile("OutputTP2.xyz");

        List<Particle> result = particles;

        for (int i = 0; i < cantRun; i ++) {
            result = selfMovingParticles.move(result);
            FileGenerator.addToOutputFile(fileOutputStream, result, i, L);
        }
        fileOutputStream.close();
        System.out.println(selfMovingParticles.polarization(result));
    }
}

