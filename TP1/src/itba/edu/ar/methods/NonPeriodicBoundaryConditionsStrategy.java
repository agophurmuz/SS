package itba.edu.ar.methods;

import itba.edu.ar.models.Cell;
import itba.edu.ar.models.Domain;
import itba.edu.ar.models.Position;

import java.util.ArrayList;
import java.util.List;

public class NonPeriodicBoundaryConditionsStrategy extends BoundaryConditionsStrategy {

    public NonPeriodicBoundaryConditionsStrategy(Domain domain, int M, int L) {
        super(domain, M, L);
    }

    @Override
    public List<Cell> getNeighborCells(int x, int y, int M) {
        List<Position> list = new ArrayList<>();
        list.add(new Position(x, y));

        if (isValidNeighbor(x, y - 1, M)) {
            list.add(new Position(x, y - 1));
        }
        if (isValidNeighbor(x + 1, y - 1, M)) {
            list.add(new Position(x + 1, y - 1));
        }
        if (isValidNeighbor(x + 1, y, M)) {
            list.add(new Position(x + 1, y));
        }
        if (isValidNeighbor(x + 1, y + 1, M)) {
            list.add(new Position(x + 1, y + 1));
        }
        return createCellList(list);
    }

    private static boolean isValidNeighbor(int x, int y, int M) {
        return (x >= 0 && y >= 0 && x <= M - 1 && y <= M - 1);
    }
}
