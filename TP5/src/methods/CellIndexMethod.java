
package methods;

import models.Cell;
import models.Domain;
import models.Particle;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
                List<Particle> particles = domain.getCellParticleList(x, y);
                if (!particles.isEmpty()) {
                    List<Cell> neighborCells = getNeighborCells(y, x, M, domain);
                    for (Particle particle : particles) {
                        if (!result.containsKey(particle)){
                            result.put(particle, new HashSet<Particle>());
                        }
                        for (Cell cell : neighborCells) {
                            for (Particle neighborParticle : cell.getParticleList()) {
                                if (particle.getId() != neighborParticle.getId()) {
                                    double dist = boundaryConditionsStrategy.calculateDistance(particle, neighborParticle, cell.getBorderType());
                                    if (dist <= rc) {
                                        result.get(particle).add(neighborParticle);
                                        if (!result.containsKey(neighborParticle)) {
                                            result.put(neighborParticle, new HashSet<Particle>());
                                        }
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

    public void resetParticles(List<Particle> nextParticles) {
        this.domain = new Domain(L, M, nextParticles);
    }
}
