package itba.edu.ar.methods;

import itba.edu.ar.models.Position;

import java.util.List;

public interface BoundaryConditionsStrategy {

     List<Position> getValidNeighborPositions(int x, int y, int M);
}
