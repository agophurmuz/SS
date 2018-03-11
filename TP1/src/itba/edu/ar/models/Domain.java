package itba.edu.ar.models;

import java.util.List;

public class Domain {

    private Cell[][] matrix;
    private int M;
    private int L;

    public Domain(int L, int M, List<Particle> particleList) {
        this.L = L;
        this.M = M;
        this.matrix = new Cell[M][M];
        populateMatrix(particleList);
    }

    private void populateMatrix(List<Particle> particleList) {
        for (int x = 0; x < M; x++) {
            for (int y = 0; y < M; y++) {
                matrix[x][y] = new Cell();
                for (Particle particle : particleList) {
                    if (isParticleInCellDomain(particle,x,y))
                        matrix[x][y].addParticle(particle);
                }
            }
        }
    }

    private boolean isParticleInCellDomain(Particle particle, int cellX, int cellY) {
        Position position = particle.getPosition();
        double vx = position.getX().doubleValue();
        double vy = position.getY().doubleValue();

        if (isVariableInCellRange(vx,cellX) && isVariableInCellRange(vy, cellY)) {
            return true;
        }

        return false;
    }

    //Range Cell x and y = (xL/M ; (L/M-1) + xL/M)
    private boolean isVariableInCellRange(double v, int cellIndex) {
        return (v > (cellIndex * L / M) && v < (L / M) + (cellIndex * L / M));
    }

    public List<Particle> getCellParticleList(int x, int y) {
        return matrix[x][y].getParticleList();
    }

    public Cell getCell(int x, int y) {
        return matrix[x][y];
    }

    @Override
    public String toString() {
        return "Domain{" +
                "matrix=" + matrixToString() +
                '}';
    }

    private String matrixToString() {
        String string = "[";
        for (int x = 0; x < M; x++) {
            for (int y = 0; y < M; y++) {
                string = string + matrix[x][y].toString() + ", ";
            }
        }
        return string + "]";
    }

}
