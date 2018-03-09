
package itba.edu.ar.methods;

import itba.edu.ar.models.Cell;
import itba.edu.ar.models.Board;
import itba.edu.ar.models.Particle;
import itba.edu.ar.models.Position;

import java.util.*;

public class CellIndexMethod {

    private BoundaryConditionsStrategy boundaryConditionsStrategy;
    private int M;
    private double rc;
    private Board board;

    public CellIndexMethod(BoundaryCondition boundaryCondition, int M, double rc, Board board) {
        changeBoundaryConditionsStrategy(boundaryCondition);
        this.M = M;
        this.rc = rc;
        this.board = board;
    }

    public void changeBoundaryConditionsStrategy(BoundaryCondition boundaryCondition){
       switch (boundaryCondition){
           case PERIODIC:
               boundaryConditionsStrategy = new PeriodicBoundaryConditionsStrategy();
               break;
           case NON_PERIODIC:
               boundaryConditionsStrategy = new NonPeriodicBoundaryConditionsStrategy();
               break;
           default:
               throw new IllegalStateException("Invalid Boundary Condition Strategy");
       }
    }

    public HashMap<Integer, Set<Integer>> calculateDistance() {

        HashMap<Integer, Set<Integer>> result = new HashMap<>();

        for (int x = 0; x < M; x++) {
            for (int y = 0; y < M; y++) {
                List<Cell> neighbors = calculateNeighborsCell(y, x, M, board);
                List<Particle> particles = board.getBoard()[x][y].getPatricleList();
                for (Particle particle : particles) {
                    for (Cell cell : neighbors) {
                        for (Particle neighborParticle : cell.getPatricleList()) {
                            if (particle.getId() != neighborParticle.getId()) {
                                double posX = neighborParticle.getPosition().getX().doubleValue() - particle.getPosition().getX().doubleValue();
                                double posY = neighborParticle.getPosition().getY().doubleValue() - particle.getPosition().getY().doubleValue();
                                double dist = (Math.sqrt(Math.pow(posX, 2) + Math.pow(posY, 2))) - (particle.getRadius() + neighborParticle.getRadius());
                                if (dist < rc) {
                                    if (result.containsKey(particle.getId())) {
                                        result.get(particle.getId()).add(neighborParticle.getId());
                                    } else {
                                        result.put(particle.getId(), new HashSet<>());
                                        result.get(particle.getId()).add(neighborParticle.getId());
                                    }
                                    if (result.containsKey(neighborParticle.getId())) {
                                        result.get(neighborParticle.getId()).add(particle.getId());
                                    } else {
                                        result.put(neighborParticle.getId(), new HashSet<>());
                                        result.get(neighborParticle.getId()).add(particle.getId());
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return result;
    }

    private List<Cell> calculateNeighborsCell(int x, int y, int M, Board board) {
        List<Cell> neighborsCellList = new ArrayList<>();

        List<Position> neighborPositions = boundaryConditionsStrategy.getValidNeighborPositions(x, y, M);
        for (Position n : neighborPositions) {
            neighborsCellList.add(board.getBoard()[n.getY().intValue()][n.getX().intValue()]);
        }

        return neighborsCellList;
    }
}
