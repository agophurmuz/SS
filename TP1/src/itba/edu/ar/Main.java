package itba.edu.ar;

import itba.edu.ar.methods.CellIndexMethod;
import itba.edu.ar.models.Board;
import itba.edu.ar.models.Cell;
import itba.edu.ar.models.Particle;
import itba.edu.ar.models.Position;

import java.util.HashMap;
import java.util.Set;

public class Main {

    public static void main(String[] args) {

        int M = 3;
        double rc = 1;
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

        board.getBoard()[0][0].addParticle(new Particle(new Position(1, 1), 1, 0.25));
        board.getBoard()[1][0].addParticle(new Particle(new Position(2, 2), 2, 0.25));
        board.getBoard()[0][2].addParticle(new Particle(new Position(3, 3), 3, 0.25));
        board.getBoard()[1][2].addParticle(new Particle(new Position(2.5, 3), 4, 0.25));
        board.getBoard()[1][1].addParticle(new Particle(new Position(1.5, 2), 5, 0.25));
        board.getBoard()[2][2].addParticle(new Particle(new Position(3.3, 3.8), 6, 0.25));
        board.getBoard()[2][0].addParticle(new Particle(new Position(4, 1.8), 7, 0.25));
        board.getBoard()[0][2].addParticle(new Particle(new Position(1.75, 1.8), 9, 0.25));

        long startTime = System.currentTimeMillis();

        HashMap<Integer, Set<Integer>> result = CellIndexMethod.calculateDistance(M, rc, board);

        long endTime = System.currentTimeMillis();

        long time = endTime - startTime;

        System.out.println("M = " + M + "tiempo: " + time);

        System.out.println(result);

    }
}
