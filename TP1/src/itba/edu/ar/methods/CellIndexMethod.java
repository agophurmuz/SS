
package itba.edu.ar.methods;

import itba.edu.ar.models.Cell;
import itba.edu.ar.models.Board;
import itba.edu.ar.models.Particle;

import java.util.*;

public class CellIndexMethod {



    public static HashMap<Integer, Set<Integer>> calculateDistance(int M, double rc, Board board) {

        HashMap<Integer, Set<Integer>> result = new HashMap<>();

        for (int x = 0; x < M ; x++){
            for (int y = 0; y < M ; y++){
                List<Cell> neighbors = calculateNeighborsCell(y, x, M, board);
                List<Particle> particles = board.getBoard()[x][y].getPatricleList();
                for (Particle particle : particles){
                    for (Cell cell : neighbors) {
                        for (Particle neighborParticle : cell.getPatricleList()) {
                            if(particle.getId() != neighborParticle.getId()){
                                double posX = neighborParticle.getPosition().getX() - particle.getPosition().getX();
                                double posY = neighborParticle.getPosition().getY() - particle.getPosition().getY();
                                double dist = (Math.sqrt(Math.pow(posX, 2) + Math.pow(posY, 2))) - (particle.getRadius() + neighborParticle.getRadius());
                                if(dist < rc){
                                    if(result.containsKey(particle.getId())){
                                        result.get(particle.getId()).add(neighborParticle.getId());
                                    } else {
                                        result.put(particle.getId(), new HashSet<>());
                                        result.get(particle.getId()).add(neighborParticle.getId());
                                    } if(result.containsKey(neighborParticle.getId())){
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

    private static List<Cell> calculateNeighborsCell(int x, int y, int M, Board board) {
        List<Cell> neighborsCellList = new ArrayList<>();
        neighborsCellList.add(board.getBoard()[y][x]);
        if (isNeighbor(x, y - 1, M)) {
            neighborsCellList.add(board.getBoard()[y - 1][x]);
        }
        if (isNeighbor(x + 1, y - 1, M)) {
            neighborsCellList.add(board.getBoard()[y - 1][x + 1]);
        }
        if (isNeighbor(x + 1, y, M)) {
            neighborsCellList.add(board.getBoard()[y][x + 1]);
        }
        if (isNeighbor(x + 1, y + 1, M)) {
            neighborsCellList.add(board.getBoard()[y + 1][x + 1]);
        }
        return neighborsCellList;
    }

    private static boolean isNeighbor(int x, int y, int M) {
        return (x >= 0 && y >= 0 && x <= M - 1 && y <= M - 1);
    }
}
