/**
 * Created by wuhao on 2017-06-01.
 */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private boolean[][] grid = new boolean[0][0];
    private WeightedQuickUnionUF qfGrid = new WeightedQuickUnionUF(0);
//        qfGridDraw = new WeightedQuickUnionUF(0);
    private int gridSize;
    private int openSitesCount = 0;

   // create n-by-n grid, with all sites blocked
    public Percolation(int n) {
        if (n < 1) {
            throw new IllegalArgumentException("the size of the percolation has to be greater than 0");
        }

        gridSize = n;
        openSitesCount = 0;
        grid = new boolean[n][n];
        qfGrid = new WeightedQuickUnionUF(n * n + 2);
//        qfGridDraw = new WeightedQuickUnionUF(n * n + 1);
    }

    // UTILITY functions
    // row, col starts with 1
    private boolean inGrid(int r1, int c1) {
        if (r1 < 1 || r1 > gridSize) {
            return false;
        } else if (c1 < 1 || c1 > gridSize) {
            return false;
        }

        return true;
    }

    // row, col starts with 1
    private int getSiteIndex(int r1, int c1) {
        return (r1 - 1) * gridSize + c1;
    }

    // open site (row, col) if it is not open already
    // passed in row & col starts at 1
    public void open(int r1, int c1) {
        int r0 = r1 - 1;
        int c0 = c1 - 1;

        if (!inGrid(r1, c1)) {
            throw new IndexOutOfBoundsException("Index does not exist while calling open.");
        }

        int currentSiteIndex = getSiteIndex(r1, c1);

        if (!grid[r0][c0]) {
            // open itself
            grid[r0][c0] = true;

            // connect it with 4 neighbors
            // up
            if (inGrid(r1 - 1, c1) && grid[r1 - 2][c1-1]) {
                int upIndex = getSiteIndex(r1 - 1, c1);

                if (!qfGrid.connected(currentSiteIndex, upIndex)) {
                    qfGrid.union(currentSiteIndex, upIndex);
//                    qfGridDraw.union(currentSiteIndex, upIndex);
                }
            }

            // down
            if (inGrid(r1 + 1, c1) && grid[r1][c1-1]) {
                int downIndex = getSiteIndex(r1 + 1, c1);

                if (!qfGrid.connected(currentSiteIndex, downIndex)) {
                    qfGrid.union(currentSiteIndex, downIndex);
//                    qfGridDraw.union(currentSiteIndex, downIndex);
                }
            }

            // left
            if (inGrid(r1, c1 - 1) && grid[r1-1][c1-2]) {
                int leftIndex = getSiteIndex(r1, c1 - 1);

                if (!qfGrid.connected(currentSiteIndex, leftIndex)) {
                    qfGrid.union(currentSiteIndex, leftIndex);
//                    qfGridDraw.union(currentSiteIndex, leftIndex);
                }
            }

            // right
            if (inGrid(r1, c1 + 1) && grid[r1-1][c1]) {
                int rightIndex = getSiteIndex(r1, c1 + 1);

                if (!qfGrid.connected(currentSiteIndex, rightIndex)) {
                    qfGrid.union(currentSiteIndex, rightIndex);
//                    qfGridDraw.union(currentSiteIndex, rightIndex);
                }
            }

            openSitesCount++;
        }
    }

    // is site (row, col) open?
    public boolean isOpen(int r1, int c1) {
        if (!inGrid(r1, c1)) {
            throw new IndexOutOfBoundsException("Index does not exist while calling isOpen. "
            + r1 + ' ' + c1);
        }

        return grid[r1 - 1][c1 - 1];
    }

    // is site (row, col) full?
    public boolean isFull(int r1, int c1) {
        if (!inGrid(r1, c1)) {
            System.out.printf("grid size: %d, row: %d, col: %d\n", gridSize, c1, r1);
            throw new IndexOutOfBoundsException("Index does not exist while calling isFull.");
        }

        // full: open site that can be connected to an open site in the top row via a chain of neighboring open sites

        // top row sites all connect to the virtual top site
        int currentSiteIndex = getSiteIndex(r1, c1);

        if (grid[r1-1][c1-1]) {
            // the top row is always full
            if (currentSiteIndex <= gridSize) return true;

            // init using the original 4 neighbors
//            int upperRowLeftIndex;
//            upperRowLeftIndex = getSiteIndex(r1 - 1, 1);
//
//            for (int i = upperRowLeftIndex; i < upperRowLeftIndex + gridSize; ++i) {
////                if (qfGridDraw.connected(currentSiteIndex, i) && qfGridDraw.connected(currentSiteIndex, 0)) {
//                if (qfGrid.connected(currentSiteIndex, i)
//                        && qfGrid.connected(currentSiteIndex, 0)) {
//                    return true;
//                }
//            }
            for (int i = 1; i <= gridSize; ++i) {
                if (qfGrid.connected(currentSiteIndex, i)) {
                    return true;
                }
            }
        }

        return false;
    }

    // number of open sites
    public int numberOfOpenSites() {
        return openSitesCount;
    }

    // does the system percolate?
    public boolean percolates() {
        // using weighted quick union
        // add virtual top 0
        int virtualTopIndex = 0;
        for (int i = 0; i < gridSize; ++i) {
            if (grid[0][i]) {
                qfGrid.union(i, virtualTopIndex);
//                qfGridDraw.union(i, virtualTopIndex);
            }
        }

        // add virtual bottom sites to the grid n*n
        int virtualBottomIndex = gridSize * gridSize + 1;
        for (int j = 0; j < gridSize; ++j) {
            if (grid[gridSize-1][j]) {
                qfGrid.union(virtualBottomIndex, getSiteIndex(gridSize, j));
            }
        }

        return qfGrid.connected(virtualBottomIndex, virtualTopIndex);
    }

    // test client (optional)
    public static void main(String[] args) {
        Percolation testTable = new Percolation(1);
        System.out.println(testTable.numberOfOpenSites());
    }
 }
