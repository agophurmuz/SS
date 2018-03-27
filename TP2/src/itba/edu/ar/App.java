package itba.edu.ar;

import itba.edu.ar.simulation.FileGenerator;
import itba.edu.ar.simulation.methods.BoundaryCondition;
import itba.edu.ar.simulation.models.Particle;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class App {

    public static void main(String[] args) throws IOException {

        int N = 400;
        int M = 12;
        double rc = 1;
        int L = 25;
        double speed = 0.03;
        double eta = 0.1;
        int cantRun = 1000;
        double radius = 0;

        List<Particle> particles = ParticleSystemGenerator.generateRandomParticleWithSpeedSystem(N, L, radius, speed);

        //long startTime = System.currentTimeMillis();
        //FileOutputStream fileOutputStreamPolarization = FileGenerator.createOutputFile("densidadValiableL10eta3bis1.csv");
        //for(int N = 50 ; N < 1001; N += 100) {
        //  List<Particle> particles = ParticleSystemGenerator.generateRandomParticleWithSpeedSystem(N, L, radius, speed);
        // SelfMovingParticles selfMovingParticles = new SelfMovingParticles(L, rc, eta, M, speed);

        //Set<Particle> result = selfMovingParticles.move(board);

        //FileGenerator.createOutputFile("OutputFile.txt", result, 8);

        //long endTime = System.currentTimeMillis();

        //long time = endTime - startTime;

        //System.out.println("M = " + M + "tiempo: " + time);

        //System.out.println(result);

        //System.out.println(selfMovingParticles.polarization(particles));

        FileOutputStream fileOutputStream = itba.edu.ar.simulation.FileGenerator.createOutputFile("OutputTP2.xyz");
        FileOutputStream fileOutputStreamPolarization = itba.edu.ar.simulation.FileGenerator.createOutputFile("PolarizationOutput.csv");

        List<Particle> result = particles;
        SelfMovingParticles selfMovingParticles = new SelfMovingParticles(L, rc, eta, M, speed);
        double p;
        for (int i = 0; i < cantRun; i++) {
            result = selfMovingParticles.move(result, BoundaryCondition.PERIODIC);
            itba.edu.ar.simulation.FileGenerator.addToOutputFile(fileOutputStream, result, i, L);
            p = selfMovingParticles.polarization(result);
            FileGenerator.addToPolarizationFile(fileOutputStreamPolarization, i, p);

        }
        fileOutputStream.close();
        fileOutputStreamPolarization.close();
        //System.out.println(selfMovingParticles.polarization(result));
        //StringBuilder sb = new StringBuilder();
        //sb.append(N/Math.pow(L, 2));
        //sb.append("\t");
        //sb.append(selfMovingParticles.polarization(result));
        //sb.append("\n");
        //fileOutputStreamPolarization.write(sb.toString().getBytes());
    }
}

