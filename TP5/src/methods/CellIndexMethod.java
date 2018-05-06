
package methods;

import models.Cell;
import models.Domain;
import models.Particle;
import models.Position;

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

    public void addWallParticleContact(Map<Particle, Set<Particle>> neighbors, List<Particle> particles){
        for (Particle p : particles) {
            List<Particle> newParticles = getWallParticleContact(p);
            if(!neighbors.containsKey(p)) {
                neighbors.put(p, new HashSet<Particle>());
            }
            neighbors.get(p).addAll(newParticles);
        }
    }

    public List<Particle> getWallParticleContact(Particle particle) {
        Particle p;
        List<Particle> result = new ArrayList<>();
        if(particle.getX() - particle.getRadius() < 0) {
            //fixme id particle, le estoy poniendo 0
            // choco con pared Izq
            double x = -particle.getRadius();
            p = new Particle(0, new Position(x, particle.getY()), 0, 0, particle.getRadius(), particle.getMass());
            p.setWall(true);
            result.add(p);

            // choco con pared Derecha
        } else if (particle.getX() + particle.getRadius() >= L) {
            double x = particle.getRadius() + L;
            p = new Particle(0, new Position(x, particle.getY()), 0, 0, particle.getRadius(), particle.getMass());
            p.setWall(true);
            result.add(p);

            // choco con pared piso
        } else if(particle.getY() - particle.getRadius() < 0) {
            double y = -particle.getRadius();
            p = new Particle(0, new Position(particle.getX(), y), 0, 0, particle.getRadius(), particle.getMass());
            p.setWall(true);
            result.add(p);

            // choco con pared techo
        } else if (particle.getY() + particle.getRadius() >= L) {
            double y = particle.getRadius() + L;
            p = new Particle(0, new Position(particle.getX(), y), 0, 0, particle.getRadius(), particle.getMass());
            p.setWall(true);
            result.add(p);
        }

        return result;
    }
}
