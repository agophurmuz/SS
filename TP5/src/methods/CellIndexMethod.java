package methods;

import models.Particle;

import java.util.*;

public class CellIndexMethod {

    private Set<Particle>[][] domain;

    private double L;
    private double rc;
    private int M;
    private double cellDimension;

    protected boolean periodicBoundaryCondition = false;


    public CellIndexMethod(boolean periodicBoundaryCondition, int M, int L, double rc, List<Particle> particles) {
        this.M = M;
        this.rc = rc;
        this.L = L;
        this.cellDimension = L / M;
        validateParameters();
        this.periodicBoundaryCondition = periodicBoundaryCondition;
        populateDomain(M, particles);
    }


    private void validateParameters() {
        if (M <= 0) {
            throw new IllegalStateException("Invalid M");
        }
        if (rc >= cellDimension) {
            throw new IllegalStateException("Invalid rc");
        }
        if (L <= 0) {
            throw new IllegalStateException("Invalid L");
        }
    }

    private void populateDomain(int M, List<? extends Particle> particles) {
        domain = new Set[M][M];
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < M; j++) {
                domain[i][j] = new HashSet<>();
            }
        }
        for (Particle p : particles) {
            domain[(int) (p.getX() / cellDimension)][(int) (p.getY() / cellDimension)].add(p);
        }
    }

    public Map<Particle, Set<Particle>> getParticleNeighbors(List<Particle> particles) {

        Map<Particle, Set<Particle>> map = new HashMap<>();

        for (Particle particle : particles) {

            if (!map.containsKey(particle))
                map.put(particle, new HashSet<>());

            int x = (int) (particle.getX() / cellDimension);
            int y = (int) (particle.getY() / cellDimension);
            Set<Particle> cell;
            if (x < 0) {
                x = 0;
            }
            if (y < 0) {
                y = 0;
            }
            if (x >= M) {
                x = M - 1;
            }
            if (y >= M) {
                y = M - 1;
            }
            cell = domain[x][y];
            addParticleNeighbors(cell, particle, map, 0, 0);

            cell = domain[(x - 1 + M) % M][y];
            if (x - 1 >= 0) {
                addParticleNeighbors(cell, particle, map, 0, 0);
            } else if (periodicBoundaryCondition) {
                addParticleNeighbors(cell, particle, map, -1, 0);
            }

            cell = domain[(x - 1 + M) % M][(y + 1) % M];
            if (x - 1 >= 0 && y + 1 < M) {
                addParticleNeighbors(cell, particle, map, 0, 0);
            } else if (periodicBoundaryCondition) {
                addParticleNeighbors(cell, particle, map, x - 1 >= 0 ? 0 : -1, y + 1 < M ? 0 : 1);
            }

            cell = domain[x][(y + 1) % M];
            if (y + 1 < M) {
                addParticleNeighbors(cell, particle, map, 0, 0);
            } else if (periodicBoundaryCondition) {
                addParticleNeighbors(cell, particle, map, 0, 1);
            }

            cell = domain[(x + 1) % M][(y + 1) % M];
            if (x + 1 < M && y + 1 < M) {
                addParticleNeighbors(cell, particle, map, 0, 0);
            } else if (periodicBoundaryCondition) {
                addParticleNeighbors(cell, particle, map, x + 1 < M ? 0 : 1, y + 1 < M ? 0 : 1);
            }
        }

        return map;
    }

    private void addParticleNeighbors(Set<Particle> c, Particle p, Map<Particle, Set<Particle>> m, int deltaX,
                                      int deltaY) {

        for (Particle candidate : c) {
            if (!candidate.equals(p) && !m.get(p).contains(candidate)) {
                double distance = Math.max(calculateDistance(p, candidate, deltaX, deltaY), 0);
                if (distance <= rc) {
                    m.get(p).add(candidate);
                    if (!m.containsKey(candidate))
                        m.put(candidate, new HashSet<>());
                    m.get(candidate).add(p);
                }

            }
        }
    }

    private double calculateDistance(Particle p1, Particle p2, int deltaX, int deltaY) {
        return Math
                .sqrt(Math.pow(p1.getX() - (p2.getX() + deltaX * L), 2) + Math.pow(p1.getY() - (p2.getY() + deltaY * L), 2))
                - p1.getRadius() - p2.getRadius();
    }

    public void resetParticles(List<Particle> particles) {
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < M; j++) {
                domain[i][j] = new HashSet<>();
            }
        }

        for (Particle p : particles) {
            int x = (int) (p.getX() / cellDimension);
            int y = (int) (p.getY() / cellDimension);
            if (x < 0) {
                x = 0;
            }
            if (y < 0) {
                y = 0;
            }
            if (x >= M) {
                x = M - 1;
            }
            if (y >= M) {
                y = M - 1;
            }
            domain[x][y].add(p);
        }
    }
}
