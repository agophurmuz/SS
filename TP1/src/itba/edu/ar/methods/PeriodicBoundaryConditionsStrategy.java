package itba.edu.ar.methods;

import itba.edu.ar.models.Cell;
import itba.edu.ar.models.Domain;
import itba.edu.ar.models.Particle;
import itba.edu.ar.models.Position;

import java.util.ArrayList;
import java.util.List;

public class PeriodicBoundaryConditionsStrategy extends BoundaryConditionsStrategy {

    public PeriodicBoundaryConditionsStrategy(Domain domain, int M, int L) {
        super(domain, M, L);
    }

    @Override
    public List<Cell> getNeighborCells(int x, int y, int M) {
        List<Position> postitionList = new ArrayList<>();
        BorderType borderType = BorderType.NO_BORDER;

        if (isTopBorder(x, y, M)) {
            postitionList.add(new Position(x, M - 1));

            //is TOP_RIGHT_CORNER
            if (isRightBorder(x, y, M)) {
                postitionList.add(new Position(0, M - 1));
                postitionList.add(new Position(0, 0));
                postitionList.add(new Position(0, 1));
                borderType = BorderType.TOP_RIGHT_CORNER;

            } else {
                //IS TOP_BORDER
                postitionList.add(new Position(x + 1, M - 1));
                postitionList.add(new Position(x + 1, y));
                postitionList.add(new Position(x + 1, y + 1));
                borderType = BorderType.TOP_BORDER;
            }

        } else if (isBottomBorder(x, y, M)) {
            postitionList.add(new Position(x, y - 1));

            //is BOTTOM_RIGHT_CORNER
            if (isRightBorder(x, y, M)) {
                postitionList.add(new Position(0, M - 2));
                postitionList.add(new Position(0, M - 1));
                postitionList.add(new Position(0, 0));
                borderType = BorderType.BOTTOM_RIGHT_CORNER;
            } else {
                postitionList.add(new Position(x + 1, y - 1));
                postitionList.add(new Position(x + 1, y));
                postitionList.add(new Position(x + 1, 0));
                borderType = BorderType.BOTTOM_BORDER;
            }


        } else if (isRightBorder(x, y, M)) {
            postitionList.add(new Position(x, y - 1));
            postitionList.add(new Position(0, y - 1));
            postitionList.add(new Position(0, y));
            postitionList.add(new Position(0, y + 1));
            borderType = BorderType.RIGHT_BORDER;

        } else {
            //NO-BORDER-CELL case
            postitionList.add(new Position(x, y - 1));
            postitionList.add(new Position(x + 1, y - 1));
            postitionList.add(new Position(x + 1, y));
            postitionList.add(new Position(x + 1, y + 1));
        }

        List<Cell> cellList = createCellList(postitionList, borderType);
        cellList.add(domain.getCell(y, x));

        return cellList;
    }

    private static boolean isBottomBorder(int x, int y, int M) {
        return x >= 0 && x < M && y == M - 1;
    }

    private static boolean isTopBorder(int x, int y, int M) {
        return x >= 0 && x < M && y == 0;
    }

    private static boolean isRightBorder(int x, int y, int M) {
        return x == M - 1 && y >= 0 && y < M;
    }

    @Override
    public double calculateDistance(Particle particle, Particle neighborParticle, BorderType borderType) {

        Position<Double> shiftedPosition = getShiftedPosition(neighborParticle.getPosition(), borderType);

        return super.calculateDistance(shiftedPosition.getX(), shiftedPosition.getY(), particle.getX(), particle.getY(), particle.getRadius(), neighborParticle.getRadius());
    }

    private Position getShiftedPosition(Position<Double> position, BorderType neighborBorderType) {
        int delta = L;
        double x = position.getX();
        double y = position.getY();

        switch (neighborBorderType) {
            case TOP_BORDER:
                //(x, y-L)
                y = y - delta;
                break;
            case TOP_RIGHT_CORNER:
                //(x+L, y-L)
                x = x + delta;
                y = y - delta;
                break;
            case RIGHT_BORDER:
                //(x+L, y)
                x = x + delta;
                break;
            case BOTTOM_BORDER:
                //(x, y+L)
                y = y + delta;
                break;
            case BOTTOM_RIGHT_CORNER:
                //(x+L,y+L)
                x = x + delta;
                y = y + delta;
                break;
        }
        return new Position<Double>(x, y);
    }

}
