
package itba.edu.ar.methods;

import itba.edu.ar.models.Cell;
import itba.edu.ar.models.Domain;
import itba.edu.ar.models.Particle;

import java.util.*;

public class CellIndexMethod {

    private BoundaryConditionsStrategy boundaryConditionsStrategy;
    private int M;
    private int L;
    private double rc;
    private Domain domain;

    public CellIndexMethod(BoundaryCondition boundaryCondition, int M, int L, double rc, List<Particle> particleList) {
        this.M = M;
        this.rc = rc;
        this.L = L;
        validateParameters();
        this.domain = new Domain(L, M, particleList);
        changeBoundaryConditionsStrategy(boundaryCondition);
    }

    public void changeBoundaryConditionsStrategy(BoundaryCondition boundaryCondition) {
        switch (boundaryCondition) {
            case PERIODIC:
                boundaryConditionsStrategy = new PeriodicBoundaryConditionsStrategy(domain, M, L);
                break;
            case NON_PERIODIC:
                boundaryConditionsStrategy = new NonPeriodicBoundaryConditionsStrategy(domain, M, L);
                break;
            default:
                throw new IllegalStateException("Invalid Boundary Condition Strategy");
        }
    }

    private void validateParameters() {
        if (M <= 0) {
            throw new IllegalStateException("Invalid M");
        }
        if (rc >= (L / M)) {
            throw new IllegalStateException("Invalid rc");
        }
        if (L <= 0) {
            throw new IllegalStateException("Invalid L");
        }
    }


    public HashMap<Particle, Set<Particle>> getParticleNeighbors() {

        HashMap<Particle, Set<Particle>> result = new HashMap<>();

        for (int x = 0; x < M; x++) {
            for (int y = 0; y < M; y++) {
                List<Cell> neighbors = getNeighborCells(y, x, M, domain);
                List<Particle> particles = domain.getCellParticleList(x, y);

                for (Particle particle : particles) {
                    for (Cell cell : neighbors) {
                        for (Particle neighborParticle : cell.getParticleList()) {
                            if (particle.getId() != neighborParticle.getId()) {
                                double dist = boundaryConditionsStrategy.calculateDistance(particle, neighborParticle, cell.getBorderType());
                                if (dist < rc) {
                                    if (result.containsKey(particle)) {
                                        result.get(particle).add(neighborParticle);
                                    } else {
                                        result.put(particle, new HashSet<>());
                                        result.get(particle).add(neighborParticle);
                                    }
                                    if (result.containsKey(neighborParticle)) {
                                        result.get(neighborParticle).add(particle);
                                    } else {
                                        result.put(neighborParticle, new HashSet<>());
                                        result.get(neighborParticle).add(particle);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return result;
    }

    private List<Cell> getNeighborCells(int x, int y, int M, Domain domain) {
        return boundaryConditionsStrategy.getNeighborCells(x, y, M);
    }
}
