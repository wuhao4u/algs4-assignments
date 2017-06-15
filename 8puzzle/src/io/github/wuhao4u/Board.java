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
        goalBoard[n - 1][n - 1] = 0;
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
        int[][] fTiles = tiles.clone();

        int ind = StdRandom.uniform(n * n);
        int rx = ind / n;
        int ry = ind - rx * n - 1;

        int[] emptyInd = emptyTileIndices();
        int ex = emptyInd[0];
        int ey = emptyInd[1];
        while (rx == ex && ry == ey) {
            ind = StdRandom.uniform(n * n);
            rx = ind / n;
            ry = ind - rx * n - 1;
        }

        if (inBoard(rx - 1, ry)) {
            // swap with the left one
            int temp = fTiles[rx - 1][ry];
            fTiles[rx - 1][ry] = fTiles[rx][ry];
            fTiles[rx][ry] = temp;
        } else {
            // swap with the right one
            int temp = fTiles[rx + 1][ry];
            fTiles[rx + 1][ry] = fTiles[rx][ry];
            fTiles[rx][ry] = temp;
        }

        Board flipped = new Board(fTiles);
        return flipped;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == null) return false;
        if (y == this) return true;
//        if (!(y instanceof Board)) return false;
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

    private boolean inBoard(int x, int y) {
        if (x < 0 || x >= n) return false;
        if (y < 0 || y >= n) return false;
        return true;
    }

    private void swapTiles(int[][] board, int x1, int y1, int x2, int y2) {
        if ((!inBoard(x1, y1)) || (!inBoard(x2, y2))) {
            throw new IllegalArgumentException("Non-exists tiles in the argument");
        }
        int temp = board[x1][y1];
        board[x1][y1] = board[x2][y2];
        board[x2][y2] = temp;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        Queue neighborsQ = new ArrayDeque();

        // get empty tile indices
        int x, y;
        int[] ind = emptyTileIndices();
        x = ind[0];
        y = ind[1];

        // left
        int lx = x - 1, ly = y;
        if (inBoard(lx, ly)) {
            int[][] lTiles = tiles.clone();
            swapTiles(lTiles, x, y, lx, ly);
            Board lb = new Board(lTiles);
            neighborsQ.add(lb);
        }

        // right
        int rx = x + 1, ry = y;
        if (inBoard(rx, ry)) {
            int[][] rTiles = tiles.clone();
            swapTiles(rTiles, x, y, rx, ry);
            Board rb = new Board(rTiles);
            neighborsQ.add(rb);
        }

        // up
        int ux = x, uy = y - 1;
        if (inBoard(ux, uy)) {
            int[][] uTiles = tiles.clone();
            swapTiles(uTiles, x, y, ux, uy);
            Board ub = new Board(uTiles);
            neighborsQ.add(ub);
        }

        // down
        int dx = x, dy = y + 1;
        if (inBoard(lx, ly)) {
            int[][] dTiles = tiles.clone();
            swapTiles(dTiles, x, y, dx, dy);
            Board ub = new Board(dTiles);
            neighborsQ.add(ub);
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

        TestBoard tb = new TestBoard(initial);
        tb.testHamming();
        tb.testManhattan();
    }
}
