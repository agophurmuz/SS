package itba.edu.ar.simulation.models;

import itba.edu.ar.simulation.methods.BorderType;

public class PositionTypeCell {

    private int x;
    private int y;
    private BorderType borderType;

    public PositionTypeCell(int x, int y, BorderType borderType) {
        this.x = x;
        this.y = y;
        this.borderType = borderType;
    }

    public PositionTypeCell(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public BorderType getBorderType() {
        return borderType;
    }

    public void setBorderType(BorderType borderType) {
        this.borderType = borderType;
    }
}
