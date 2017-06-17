package io.github.wuhao4u;

import java.util.*;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdRandom;

/**
 * Created by wuhao on 2017-06-14.
 */

public class Board {
    private int n; // this is the dimension of the board, not total number of tiles
    private int[][] goalBoard;
    private int[][] tiles;

    // construct a board from an n-by-n array of blocks
    // (where blocks[i][j] = block in row i, column j)
    public Board(int[][] blocks) {
        if (blocks == null) throw new NullPointerException("Give me a block please!");
        n = blocks.length;
        goalBoard = new int[n][n];

        tiles = copy2DArray(blocks);

        // generate a goal board, 1 ~ n^2-1
        int tileNum = 1;
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                goalBoard[i][j] = tileNum++;
            }
        }
        goalBoard[n - 1][n - 1] = 0;
    }

    private int[][] copy2DArray(int[][] origin) {
        final int[][] copiedArray = new int[origin.length][origin.length];
        for (int i = 0; i < origin.length; i++) {
            copiedArray[i] = Arrays.copyOf(origin[i], origin[i].length);
        }
        return copiedArray;
    }

    // board dimension n
    public int dimension() {
        return n;
    }

    // number of blocks out of place
    public int hamming() {
        int hamSum = 0;
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                if (tiles[i][j] == 0) continue;
                if (tiles[i][j] != goalBoard[i][j]) ++hamSum;
            }
        }
        return hamSum;
    }

    // sum of Manhattan distances between blocks and goal
    public int manhattan() {
        int manhattanSum = 0;
        int correctX, correctY;
        int xDiff, yDiff;

        // where is the correct position?
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                // empty tile does not count
                int val = tiles[i][j];
                if (val == 0) continue;

                if (val % n == 0) {
                    // last item in the row, right edge
                    correctY = val / n - 1;
                } else {
                    correctY = val / n;
                }
                correctX = val - correctY * n - 1;

                // absolute difference between current x, y to the correct x, y is the manhattan val
                yDiff = Math.abs(i - correctY);
                xDiff = Math.abs(j - correctX);

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
        int[][] fTiles = copy2DArray(tiles);

        int ri, rj;

        while (true) {
            ri = StdRandom.uniform(n);
            rj = StdRandom.uniform(n);

            if (fTiles[ri][rj] == 0) continue;

            if (inBoard(ri, rj - 1) && fTiles[ri][rj - 1] != 0) {
                // swap with the left one
                swapTiles(fTiles, ri, rj, ri, rj-1);
                break;
            }

            if (inBoard(ri, rj + 1) && fTiles[ri][rj+1] != 0) {
                // swap with the right one
                swapTiles(fTiles, ri, rj, ri, rj+1);
                break;
            }
        }

        Board flipped = new Board(fTiles);
        return flipped;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == null) return false;
        if (y == this) return true;
        if (this.getClass() != y.getClass()) return false;

        Board that = (Board) y;
        if (that.dimension() != n) {
            return false;
        } else {
            for (int i = 0; i < n; ++i) {
                for (int j = 0; j < n; ++j) {
                    if (this.tiles[i][j] != that.tiles[i][j]) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    private int[] emptyTileIndices() {
        int[] ind = {-1, -1};

        // find the empty tile indices
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                if (tiles[i][j] == 0) {
                    ind[0] = i;
                    ind[1] = j;
                }
            }
        }

        if (ind[0] < 0 || ind[1] < 0) {
            throw new IllegalArgumentException("Board missing empty tile.");
        }

        return ind;
    }

    private boolean inBoard(int i, int j) {
        if (i < 0 || i >= n) return false;
        if (j < 0 || j >= n) return false;
        return true;
    }

    private void swapTiles(int[][] board, int i1, int j1, int i2, int j2) {
        if ((!inBoard(i1, j1)) || (!inBoard(i2, j2))) {
            throw new IllegalArgumentException("Non-exists tiles in the argument");
        }
        int temp = board[i1][j1];
        board[i1][j1] = board[i2][j2];
        board[i2][j2] = temp;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        ArrayDeque<Board> neighborsQ = new ArrayDeque<Board>();

        // get empty tile indices
        int i, j;
        int[] ind = emptyTileIndices();
        i = ind[0];
        j = ind[1];

        int lj = j - 1, li = i;
        int rj = j + 1, ri = i;
        int uj = j, ui = i - 1;
        int dj = j, di = i + 1;


        // left
        if (inBoard(li, lj)) {
            int[][] lTiles = copy2DArray(tiles);
            swapTiles(lTiles, i, j, li, lj);
            Board lb = new Board(lTiles);
            neighborsQ.add(lb);
        }

        // right
        if (inBoard(ri, rj)) {
            int[][] rTiles = copy2DArray(tiles);
            swapTiles(rTiles, i, j, ri, rj);
            Board rb = new Board(rTiles);
            neighborsQ.add(rb);
        }

        // up
        if (inBoard(ui, uj)) {
            int[][] uTiles = copy2DArray(tiles);
            swapTiles(uTiles, i, j, ui, uj);
            Board ub = new Board(uTiles);
            neighborsQ.add(ub);
        }

        // down
        if (inBoard(di, dj)) {
            int[][] dTiles = copy2DArray(tiles);
            swapTiles(dTiles, i, j, di, dj);
            Board db = new Board(dTiles);
            neighborsQ.add(db);
        }

        return neighborsQ;
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
    public static void main(String[] args) {
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();

        Board initial = new Board(blocks);

//        TestBoard tb = new TestBoard(initial);
//        tb.testHamming();
//        tb.testManhattan();
//        tb.testIsGoal();
//        tb.testTwin();
//        tb.testEquals();
//        tb.testNeighbors();
    }
}
