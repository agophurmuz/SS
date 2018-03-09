package itba.edu.ar;

import itba.edu.ar.methods.BoundaryCondition;
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
        double rc = 10;
        int L = 30;
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

        board.getBoard()[0][0].addParticle(new Particle(new Position(1, 1), 1, 0.1));
        board.getBoard()[0][1].addParticle(new Particle(new Position(12, 2), 2, 0.2));
        board.getBoard()[2][0].addParticle(new Particle(new Position(3, 23), 3, 0.3));
        board.getBoard()[2][1].addParticle(new Particle(new Position(11, 22), 4, 0.4));
        board.getBoard()[1][1].addParticle(new Particle(new Position(15, 15), 5, 0.5));
        board.getBoard()[2][2].addParticle(new Particle(new Position(25, 24), 6, 0.6));
        board.getBoard()[0][2].addParticle(new Particle(new Position(24, 1), 7, 0.07));
        board.getBoard()[2][0].addParticle(new Particle(new Position(1, 28), 9, 0.8));

        long startTime = System.currentTimeMillis();

        CellIndexMethod cellIndexMethod = new CellIndexMethod(BoundaryCondition.PERIODIC,M, rc, board);

        HashMap<Integer, Set<Integer>> result = cellIndexMethod.calculateDistance();

        FileGenerator.createOutputFile("OutputFile.txt", result, 8);

        long endTime = System.currentTimeMillis();

        long time = endTime - startTime;

        System.out.println("M = " + M + "tiempo: " + time);

        System.out.println(result);

    }
}
