package itba.edu.ar.simulation.methods;

import itba.edu.ar.simulation.models.*;

import java.util.ArrayList;
import java.util.List;

public class PeriodicBoundaryConditionsStrategy extends BoundaryConditionsStrategy {

    public PeriodicBoundaryConditionsStrategy(Domain domain, int M, int L) {
        super(domain, M, L);
    }

    @Override
    public List<Cell> getNeighborCells(int x, int y, int M) {
        List<PositionTypeCell> positionList = new ArrayList<>();
        positionList.add(new PositionTypeCell(x, y, BorderType.NO_BORDER));
        if (isTopBorder(x, y, M)) {
            positionList.add(new PositionTypeCell(x, M - 1, BorderType.TOP_BORDER));

            //is TOP_RIGHT_CORNER
            if (isRightBorder(x, y, M)) {
                positionList.add(new PositionTypeCell(0, M - 1, BorderType.TOP_RIGHT_CORNER));
                positionList.add(new PositionTypeCell(0, 0, BorderType.RIGHT_BORDER));
                positionList.add(new PositionTypeCell(0, 1, BorderType.RIGHT_BORDER));

            } else {
                //IS TOP_BORDER
                positionList.add(new PositionTypeCell(x + 1, M - 1, BorderType.TOP_BORDER));
                positionList.add(new PositionTypeCell(x + 1, y, BorderType.NO_BORDER));
                positionList.add(new PositionTypeCell(x + 1, y + 1, BorderType.NO_BORDER));
            }

        } else if (isBottomBorder(x, y, M)) {
            positionList.add(new PositionTypeCell(x, y - 1, BorderType.NO_BORDER));

            //is BOTTOM_RIGHT_CORNER
            if (isRightBorder(x, y, M)) {
                positionList.add(new PositionTypeCell(0, M - 2, BorderType.RIGHT_BORDER));
                positionList.add(new PositionTypeCell(0, M - 1, BorderType.RIGHT_BORDER));
                positionList.add(new PositionTypeCell(0, 0, BorderType.BOTTOM_RIGHT_CORNER));
            } else {
                //is BOTTOM_BORDER
                positionList.add(new PositionTypeCell(x + 1, y - 1, BorderType.NO_BORDER));
                positionList.add(new PositionTypeCell(x + 1, y, BorderType.NO_BORDER));
                positionList.add(new PositionTypeCell(x + 1, 0, BorderType.BOTTOM_BORDER));
            }


        } else if (isRightBorder(x, y, M)) {
            positionList.add(new PositionTypeCell(x, y - 1, BorderType.NO_BORDER));
            positionList.add(new PositionTypeCell(0, y - 1, BorderType.RIGHT_BORDER));
            positionList.add(new PositionTypeCell(0, y, BorderType.RIGHT_BORDER));
            positionList.add(new PositionTypeCell(0, y + 1, BorderType.RIGHT_BORDER));

        } else {
            //NO-BORDER-CELL case
            positionList.add(new PositionTypeCell(x, y - 1, BorderType.NO_BORDER));
            positionList.add(new PositionTypeCell(x + 1, y - 1, BorderType.NO_BORDER));
            positionList.add(new PositionTypeCell(x + 1, y, BorderType.NO_BORDER));
            positionList.add(new PositionTypeCell(x + 1, y + 1, BorderType.NO_BORDER));
        }

        //List<Cell> cellList = createCellList(positionList);
        //cellList.add(domain.getCell(y, x));

        return createCellList(positionList);
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
                x = x - delta;
                break;
            case TOP_RIGHT_CORNER:
                //(x+L, y-L)
                y = y + delta;
                x = x - delta;
                break;
            case RIGHT_BORDER:
                //(x+L, y)
                y = y + delta;
                break;
            case BOTTOM_BORDER:
                //(x, y+L)
                x = x + delta;
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
