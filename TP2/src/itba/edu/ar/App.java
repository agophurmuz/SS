/*
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

        int M = 3;
        double rc = 8;
        int L = 30;
        double speed = 0.3;
        double pi = Math.PI;
        double eta = 0.1;
        int cantRun = 100;
        Board board = new Board(M);
        board.getBoard()[0][0] = new Cell();
        board.getBoard()[0][1] = new Cell();
        board.getBoard()[0][2] = new Cell();
        board.getBoard()[1][0] = new Cell();
        board.getBoard()[1][1] = new Cell();
        board.getBoard()[1][2] = new Cell();
        board.getBoard()[2][0] = new Cell();
        board.getBoard()[2][1] = new Cell();
        board.getBoard()[2][2] = new Cell();

        board.getBoard()[0][0].addParticle(new Particle(new Position(1, 1), 1, 0.1, Math.random() * 2 * pi, speed));
        board.getBoard()[0][1].addParticle(new Particle(new Position(12, 2), 2, 0.2, Math.random() * 2 * pi, speed));
        board.getBoard()[2][0].addParticle(new Particle(new Position(3, 23), 3, 0.3, Math.random() * 2 * pi, speed));
        board.getBoard()[2][1].addParticle(new Particle(new Position(11, 22), 4, 0.4, Math.random() * 2 * pi, speed));
        board.getBoard()[1][1].addParticle(new Particle(new Position(15, 15), 5, 0.5, Math.random() * 2 * pi, speed));
        board.getBoard()[2][2].addParticle(new Particle(new Position(25, 24), 6, 0.6, Math.random() * 2 * pi, speed));
        board.getBoard()[0][2].addParticle(new Particle(new Position(24, 1), 7, 0.07, Math.random() * 2 * pi, speed));
        board.getBoard()[2][0].addParticle(new Particle(new Position(1, 28), 9, 0.8, Math.random() * 2 * pi, speed));

        List<Particle> particles = new ArrayList<>();
        particles.add(new Particle(new Position(1, 1), 1, 0.1, Math.random() * 2 * pi, speed));
        particles.add(new Particle(new Position(12, 2), 2, 0.2, Math.random() * 2 * pi, speed));
        particles.add(new Particle(new Position(3, 23), 3, 0.3, Math.random() * 2 * pi, speed));
        particles.add(new Particle(new Position(11, 22), 4, 0.4, Math.random() * 2 * pi, speed));
        particles.add(new Particle(new Position(15, 15), 5, 0.5, Math.random() * 2 * pi, speed));
        particles.add(new Particle(new Position(25, 24), 6, 0.6, Math.random() * 2 * pi, speed));
        particles.add(new Particle(new Position(24, 1), 7, 0.07, Math.random() * 2 * pi, speed));
        particles.add(new Particle(new Position(1, 28), 9, 0.8, Math.random() * 2 * pi, speed));

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

        Set<Particle> result = null;

        for (int i = 0; i < cantRun; i ++) {
            result = selfMovingParticles.move(board);
            FileGenerator.addToOutputFile(fileOutputStream, result, i, L);
        }
        fileOutputStream.close();
        System.out.println(selfMovingParticles.polarization(result));
    }
}
*/
