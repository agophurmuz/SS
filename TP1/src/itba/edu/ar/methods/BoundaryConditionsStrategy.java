package itba.edu.ar.methods;

import itba.edu.ar.models.Cell;
import itba.edu.ar.models.Domain;
import itba.edu.ar.models.Position;

import java.util.ArrayList;
import java.util.List;

public abstract class BoundaryConditionsStrategy {

    protected Domain domain;
    protected int M;
    protected int L;

    public BoundaryConditionsStrategy(Domain domain, int M, int L) {
        this.domain = domain;
        this.L = L;
        this.M = M;
    }

    protected List<Cell> createCellList(List<Position> neighborPositions) {
        List<Cell> neighborsCellList = new ArrayList<>();

        for (Position n : neighborPositions) {
            neighborsCellList.add(domain.getCell(n.getY().intValue(), n.getX().intValue()));
        }

        return neighborsCellList;
    }

    public abstract List<Cell> getNeighborCells(int x, int y, int M);
}
