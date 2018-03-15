package itba.edu.ar.methods;

import itba.edu.ar.models.Cell;
import itba.edu.ar.models.Domain;
import itba.edu.ar.models.Particle;
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

    protected List<Cell> createCellList(List<Position> neighborPositions, BorderType borderType) {
        List<Cell> neighborsCellList = new ArrayList<>();
        Cell cell;
        for (Position n : neighborPositions) {
            cell = domain.getCell(n.getY().intValue(), n.getX().intValue());
            cell.setBorderType(borderType);
            neighborsCellList.add(cell);
        }

        return neighborsCellList;
    }

    public double calculateDistance(double neighborX, double neighborY, double particleX, double particleY, double particleRadius, double neighborRadius) {
        double x = neighborX - particleX;
        double y = neighborY - particleY;
        return (Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2))) - (particleRadius + neighborRadius);
    }

    public abstract List<Cell> getNeighborCells(int x, int y, int M);

    public abstract double calculateDistance(Particle particle, Particle neighborParticle, BorderType borderType);
}
