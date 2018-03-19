package itba.edu.ar.methods;

import itba.edu.ar.models.*;

import java.util.ArrayList;
import java.util.List;

public class PeriodicBoundaryConditionsStrategy extends BoundaryConditionsStrategy {

    public PeriodicBoundaryConditionsStrategy(Domain domain, int M, int L) {
        super(domain, M, L);
    }

    @Override
    public List<Cell> getNeighborCells(int x, int y, int M) {
        List<PositionTypeCell> postitionList = new ArrayList<>();

        if (isTopBorder(x, y, M)) {
            postitionList.add(new PositionTypeCell(x, M - 1, BorderType.TOP_BORDER));

            //is TOP_RIGHT_CORNER
            if (isRightBorder(x, y, M)) {
                postitionList.add(new PositionTypeCell(0, M - 1, BorderType.TOP_RIGHT_CORNER));
                postitionList.add(new PositionTypeCell(0, 0, BorderType.RIGHT_BORDER));
                postitionList.add(new PositionTypeCell(0, 1, BorderType.RIGHT_BORDER));

            } else {
                //IS TOP_BORDER
                postitionList.add(new PositionTypeCell(x + 1, M - 1, BorderType.TOP_BORDER));
                postitionList.add(new PositionTypeCell(x + 1, y, BorderType.NO_BORDER));
                postitionList.add(new PositionTypeCell(x + 1, y + 1, BorderType.NO_BORDER));
            }

        } else if (isBottomBorder(x, y, M)) {
            postitionList.add(new PositionTypeCell(x, y - 1, BorderType.NO_BORDER));

            //is BOTTOM_RIGHT_CORNER
            if (isRightBorder(x, y, M)) {
                postitionList.add(new PositionTypeCell(0, M - 2, BorderType.RIGHT_BORDER));
                postitionList.add(new PositionTypeCell(0, M - 1, BorderType.RIGHT_BORDER));
                postitionList.add(new PositionTypeCell(0, 0, BorderType.BOTTOM_RIGHT_CORNER));
            } else {
                //is BOTTOM_BORDER
                postitionList.add(new PositionTypeCell(x + 1, y - 1, BorderType.NO_BORDER));
                postitionList.add(new PositionTypeCell(x + 1, y, BorderType.NO_BORDER));
                postitionList.add(new PositionTypeCell(x + 1, 0, BorderType.BOTTOM_BORDER));
            }


        } else if (isRightBorder(x, y, M)) {
            postitionList.add(new PositionTypeCell(x, y - 1, BorderType.NO_BORDER));
            postitionList.add(new PositionTypeCell(0, y - 1, BorderType.RIGHT_BORDER));
            postitionList.add(new PositionTypeCell(0, y, BorderType.RIGHT_BORDER));
            postitionList.add(new PositionTypeCell(0, y + 1, BorderType.RIGHT_BORDER));

        } else {
            //NO-BORDER-CELL case
            postitionList.add(new PositionTypeCell(x, y - 1, BorderType.NO_BORDER));
            postitionList.add(new PositionTypeCell(x + 1, y - 1, BorderType.NO_BORDER));
            postitionList.add(new PositionTypeCell(x + 1, y, BorderType.NO_BORDER));
            postitionList.add(new PositionTypeCell(x + 1, y + 1, BorderType.NO_BORDER));
        }

        List<Cell> cellList = createCellList(postitionList);
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
