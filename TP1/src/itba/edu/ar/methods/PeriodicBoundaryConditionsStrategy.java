package itba.edu.ar.methods;

import itba.edu.ar.models.Position;

import java.util.ArrayList;
import java.util.List;

public class PeriodicBoundaryConditionsStrategy implements BoundaryConditionsStrategy {

    @Override
    public  List<Position> getValidNeighborPositions(int x, int y, int M) {
        List<Position> list = new ArrayList<>();
        list.add(new Position(x, y));
        if (isTopBorder(x, y, M)) {
            list.add(new Position(x, M - 1));

            //is TOP_RIGHT_CORNER
            if (isRightBorder(x, y, M)) {
                list.add(new Position(0, M - 1));
                list.add(new Position(0, 0));
                list.add(new Position(0, 1));
                return list;
            }
            list.add(new Position(x + 1, M - 1));
            list.add(new Position(x + 1, y));
            list.add(new Position(x + 1, y + 1));
            return list;
        }

        if (isBottomBorder(x, y, M)) {
            list.add(new Position(x, y - 1));

            //is BOTTOM_RIGHT_CORNER
            if (isRightBorder(x, y, M)) {
                list.add(new Position(0, M - 2));
                list.add(new Position(0, M - 1));
                list.add(new Position(0, 0));
                return list;
            }

            list.add(new Position(x + 1, y - 1));
            list.add(new Position(x + 1, y));
            list.add(new Position(x + 1, 0));
            return list;
        }

        if (isRightBorder(x, y, M)) {
            list.add(new Position(x, y - 1));
            list.add(new Position(0, y - 1));
            list.add(new Position(0, y));
            list.add(new Position(0, y + 1));
            return list;
        }

        list.add(new Position(x, y - 1));
        list.add(new Position(x + 1, y - 1));
        list.add(new Position(x + 1, y));
        list.add(new Position(x + 1, y + 1));
        return list;
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
}
