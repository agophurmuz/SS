package itba.edu.ar.models;

public class Position {

    private double x;
    private double y;

    public Position(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getEuclideanDistance(Position otherPosition){
        return Math.sqrt(Math.pow(otherPosition.x-x,2) + Math.pow(otherPosition.y-y,2));
    }
}
