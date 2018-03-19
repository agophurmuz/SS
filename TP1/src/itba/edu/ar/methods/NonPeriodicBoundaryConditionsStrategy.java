package itba.edu.ar.methods;

import itba.edu.ar.models.*;

import javax.swing.border.Border;
import java.util.ArrayList;
import java.util.List;

public class NonPeriodicBoundaryConditionsStrategy extends BoundaryConditionsStrategy {

    public NonPeriodicBoundaryConditionsStrategy(Domain domain, int M, int L) {
        super(domain, M, L);
    }

    @Override
    public List<Cell> getNeighborCells(int x, int y, int M) {
        List<PositionTypeCell> list = new ArrayList<>();
        list.add(new PositionTypeCell(x, y));

        if (isValidNeighbor(x, y - 1, M)) {
            list.add(new PositionTypeCell(x, y - 1));
        }
        if (isValidNeighbor(x + 1, y - 1, M)) {
            list.add(new PositionTypeCell(x + 1, y - 1));
        }
        if (isValidNeighbor(x + 1, y, M)) {
            list.add(new PositionTypeCell(x + 1, y));
        }
        if (isValidNeighbor(x + 1, y + 1, M)) {
            list.add(new PositionTypeCell(x + 1, y + 1));
        }
        return createCellList(list);
    }

    @Override
    public double calculateDistance(Particle particle, Particle neighborParticle, BorderType borderType) {
        return calculateDistance(neighborParticle.getX(), neighborParticle.getY(),particle.getX(),particle.getY(),particle.getRadius(), neighborParticle.getRadius());
    }

    private static boolean isValidNeighbor(int x, int y, int M) {
        return (x >= 0 && y >= 0 && x <= M - 1 && y <= M - 1);
    }
}
