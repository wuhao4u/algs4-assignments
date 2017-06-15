package io.github.wuhao4u;

import edu.princeton.cs.algs4.Queue;

import java.util.Collection;
import java.util.Iterator;

/**
 * Created by wuhao on 2017-06-14.
 */

public class Board {
    private int n; // this is the dimension of the board, not total number of tiles
    private int[][] goalBoard;
    public int[][] tiles;

    // construct a board from an n-by-n array of blocks
    // (where blocks[i][j] = block in row i, column j)
    public Board(int[][] blocks) {
        if (blocks == null) throw new NullPointerException("Give me a block please!");
        n = blocks.length;
        goalBoard = new int[n][n];

        tiles = blocks.clone();

        // generate a goal board, 1 ~ n^2-1
        int tileNum = 1;
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                goalBoard[i][j] = tileNum++;
            }
        }
        goalBoard[n-1][n-1] = 0;


    }

    // board dimension n
    public int dimension() {
        return n;
    }

    // number of blocks out of place
    public int hamming() {
        int ret = 0;
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                if (tiles[i][j] != goalBoard[i][j]) ++ret;
            }
        }
        return ret;
    }

    // sum of Manhattan distances between blocks and goal
    public int manhattan() {
        int manhattanSum = 0;
        int correctX, correctY;
        int xDiff, yDiff;

        // where is the correct position?
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                correctY = tiles[i][j] / n;
                correctX = tiles[i][j] - correctY * n - 1;

                // absolute difference between current x, y to the correct x, y is the manhattan val
                yDiff = Math.abs((j+1) - correctY);
                xDiff = Math.abs((i+1) - correctX);

                manhattanSum += yDiff + xDiff;
            }
        }


        return manhattanSum;
    }

    // is this board the goal board?
    public boolean isGoal() {
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                if (tiles[i][j] != goalBoard[i][j]) return false;
            }
        }
        return true;
    }

    // a board that is obtained by exchanging any pair of blocks
    public Board twin() {
        int[][] fTiles = tiles.clone();

        // TODO: make this more sophiscated
        // flip 0,0 and 0,1
        int temp = fTiles[0][0];
        fTiles[0][0] = fTiles[0][1];
        fTiles[0][1] = temp;

        Board flipped = new Board(fTiles);
        return flipped;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == null) return false;
        if (y == this) return true;
        if (!(y instanceof Board)) return false;

        Board that = (Board) y;
        if (that.dimension() == n) {
            for (int i = 0; i < n; ++i) {
                for (int j = 0; j < n; ++j) {
                    if (this.tiles[i][j] != that.tiles[i][j]) {
                        return false;
                    }
                }
            }
        } else {
            return false;
        }
        return true;
    }

    // all neighboring boards
    // TODO: fill in 4 neighbors in a queue
    public Iterable<Board> neighbors() {
        Queue ret = new Queue();

        return ret;
    }

    // string representation of this board (in the output format specified below)
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(n + "\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                s.append(String.format("%2d ", tiles[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    // unit tests (not graded)
    public static void main(String[] args) {}
}
