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
        List<Cell> cellList = new ArrayList<>();

        if (isTopBorder(x, y, M)) {
            postitionList.add(new Position(x, M - 1));

            //is TOP_RIGHT_CORNER
            if (isRightBorder(x, y, M)) {
                postitionList.add(new Position(0, M - 1));
                postitionList.add(new Position(0, 0));
                postitionList.add(new Position(0, 1));
                cellList = createCellList(postitionList, BorderType.TOP_RIGHT_CORNER);
            } else {
                postitionList.add(new Position(x + 1, M - 1));
                postitionList.add(new Position(x + 1, y));
                postitionList.add(new Position(x + 1, y + 1));
                cellList = createCellList(postitionList, BorderType.TOP_BORDER);
            }

        } else if (isBottomBorder(x, y, M)) {
            postitionList.add(new Position(x, y - 1));

            //is BOTTOM_RIGHT_CORNER
            if (isRightBorder(x, y, M)) {
                postitionList.add(new Position(0, M - 2));
                postitionList.add(new Position(0, M - 1));
                postitionList.add(new Position(0, 0));
                cellList = createCellList(postitionList, BorderType.BOTTOM_RIGHT_CORNER);
            } else {
                postitionList.add(new Position(x + 1, y - 1));
                postitionList.add(new Position(x + 1, y));
                postitionList.add(new Position(x + 1, 0));
                cellList = createCellList(postitionList, BorderType.BOTTOM_BORDER);
            }


        } else if (isRightBorder(x, y, M)) {
            postitionList.add(new Position(x, y - 1));
            postitionList.add(new Position(0, y - 1));
            postitionList.add(new Position(0, y));
            postitionList.add(new Position(0, y + 1));
            cellList = createCellList(postitionList, BorderType.RIGHT_BORDER);

        } else {
            //NO-BORDER-CELL case
            postitionList.add(new Position(x, y - 1));
            postitionList.add(new Position(x + 1, y - 1));
            postitionList.add(new Position(x + 1, y));
            postitionList.add(new Position(x + 1, y + 1));
            cellList = createCellList(postitionList);
        }

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

    protected List<Cell> createCellList(List<Position> list, BorderType borderType) {
        List<Cell> cellList = createCellList(list);
        //TODO
        Position position = null;
        double x, y;
        int delta = L;
        for (Cell cell : cellList) {
            for (Particle particle : cell.getParticleList()) {
                position = particle.getPosition();
                x = position.getX().doubleValue();
                y = position.getY().doubleValue();
                switch (borderType) {
                    case TOP_BORDER:
                        //(x, y-L)
                        position.setY(y - delta);
                        break;
                    case TOP_RIGHT_CORNER:
                        //(x+L, y-L)
                        position.setX(x + delta);
                        position.setY(y - delta);
                        break;
                    case RIGHT_BORDER:
                        //(x+L, y)
                        position.setX(x + delta);
                        break;
                    case BOTTOM_BORDER:
                        //(x, y+L)
                        position.setY(y + delta);
                        break;
                    case BOTTOM_RIGHT_CORNER:
                        //(x+L,y+L)
                        position.setX(x + delta);
                        position.setY(y + delta);
                        break;
                }
                particle.setPosition(position);
            }
        }

        return cellList;
    }
}
