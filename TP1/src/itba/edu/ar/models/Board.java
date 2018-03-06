package itba.edu.ar.models;

public class Board {

    private Cell[][] board;

    public Board(int M) {
        this.board= new Cell[M][M];
    }

    public Cell[][] getBoard() {
        return board;
    }

    public void setBoard(Cell[][] matrix) {
        this.board = matrix;
    }
}
