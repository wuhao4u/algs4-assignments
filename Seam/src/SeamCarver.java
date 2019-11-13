import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.Queue;

import java.awt.Color;
import java.util.Arrays;

public class SeamCarver {
    private class Point {
        int r, c;

        public Point(int row, int col) {
            this.r = row;
            this.c = col;
        }
    }

    private static final double BOUNDARY_ENERGY = 1000.0;
    private double[][] energyMatrix;
    private Color[][] colorMatrix;

    // create a seam carver object based on the given picture
    public SeamCarver(final Picture p) {
        if (p == null) throw new IllegalArgumentException("Picture p is null in SeamCarver constructor");

        // init color matrix
        this.colorMatrix = new Color[p.height()][p.width()];
        for (int r = 0; r < p.height(); ++r) {
            for (int c = 0; c < p.width(); ++c) {
                colorMatrix[r][c] = p.get(c, r);
            }
        }

        // init energy matrix
        this.energyMatrix = getEnergyMatrix();
    }

    // current picture
    public Picture picture() {
        Picture p = new Picture(width(), height());
        for (int r = 0; r < height(); ++r) {
            for (int c = 0; c < width(); ++c) {
                p.set(c, r, this.colorMatrix[r][c]);
            }
        }
        return p;
    }


    // width of current picture
    public int width() {
        return colorMatrix[0].length;
    }

    // height of current picture
    public int height() {
        return colorMatrix.length;
    }

    private boolean isOnBoundary(int c, int r) {
        return c == 0 || c == width() - 1 || r == 0 || r == height() - 1;
    }

    private boolean isOutOfBoundries(int c, int r, double[][] matrix) {
        return r < 0 || r > matrix.length - 1 || c < 0 || c > matrix[0].length - 1;
    }

    private int deltaXSquare(int c, int r) {
        if (c <= 0 || c >= width() - 1) throw new IllegalArgumentException("Invalid c value");
        if (r <= 0 || r >= height() - 1) throw new IllegalArgumentException("Invalid r value");

        Color rightPx = this.colorMatrix[r][c + 1];
        Color leftPx = this.colorMatrix[r][c - 1];

        int rDiff = rightPx.getRed() - leftPx.getRed();
        int gDiff = rightPx.getGreen() - leftPx.getGreen();
        int bDiff = rightPx.getBlue() - leftPx.getBlue();

        return rDiff * rDiff + gDiff * gDiff + bDiff * bDiff;
    }

    private int deltaYSquare(int c, int r) {
        if (c < 0 || c > width() - 1) throw new IllegalArgumentException("Invalid c value");
        if (r < 0 || r > height() - 1) throw new IllegalArgumentException("Invalid r value");

        Color upPx = this.colorMatrix[r - 1][c];
        Color belowPx = this.colorMatrix[r + 1][c];

        int rDiff = upPx.getRed() - belowPx.getRed();
        int gDiff = upPx.getGreen() - belowPx.getGreen();
        int bDiff = upPx.getBlue() - belowPx.getBlue();

        return rDiff * rDiff + gDiff * gDiff + bDiff * bDiff;
    }

    // energy of pixel at column x and row y
    public double energy(int c, int r) {
        if (c < 0 || c > width() - 1) throw new IllegalArgumentException("Invalid c value");
        if (r < 0 || r > height() - 1) throw new IllegalArgumentException("Invalid r value");

        if (isOnBoundary(c, r)) {
            return BOUNDARY_ENERGY;
        } else {
            return Math.sqrt(deltaXSquare(c, r) + deltaYSquare(c, r));
        }
    }

    private double[][] getEnergyMatrix() {
        double[][] matrix = new double[height()][width()];

        for (int r = 0; r < height(); ++r) {
            for (int c = 0; c < width(); ++c) {
                matrix[r][c] = energy(c, r);
            }
        }

        return matrix;
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        // construct a 2d energy array using energy() method
        // use PrintSeams to test code
        // top->down path with fewest energy

        this.energyMatrix = getEnergyMatrix();

        // the return result
        int[] seam = new int[height()];

        // lowest total energy so far
        // 1st row is always 1000
        double[][] distTo = new double[height()][width()];
        Arrays.fill(distTo[0], 0);

        // previous row's index for the result seam, for back trace in the end
        int[][] edgeTo = new int[height()][width()];
        Arrays.fill(edgeTo[0], -1);

        // fill the current energy to be the largest
        for (int r = 1; r < height(); ++r) {
            for (int c = 0; c < width(); ++c) {
                distTo[r][c] = Double.POSITIVE_INFINITY;
            }
        }

        Queue<Point> queue = new Queue<>();
        for (int c = 0; c < width(); ++c) {
            // try start from every entry in the top row

            queue.enqueue(new Point(0, c));

            while (!queue.isEmpty()) {
                Point p = queue.dequeue();

                // next pixel's energy will be sum(previous PXs) + E(x, y)
                double newEnergy = distTo[p.r][p.c] + energyMatrix[p.r][p.c];

                // try to relax below 3 pixels (x-1, y+1), (x, y+1), (x+1, y+1)
                if (!isOutOfBoundries(p.c - 1, p.r + 1, energyMatrix)) {
                    if (newEnergy < distTo[p.r + 1][p.c - 1]) {
                        // bottom-left pixel can be relaxed
                        distTo[p.r + 1][p.c - 1] = newEnergy;
                        edgeTo[p.r + 1][p.c - 1] = p.c;
                        queue.enqueue(new Point(p.r + 1, p.c - 1));
                    }
                }

                if (!isOutOfBoundries(p.c, p.r + 1, energyMatrix)) {
                    if (newEnergy < distTo[p.r + 1][p.c]) {
                        // below pixel
                        distTo[p.r + 1][p.c] = newEnergy;
                        edgeTo[p.r + 1][p.c] = p.c;
                        queue.enqueue(new Point(p.r + 1, p.c));
                    }
                }

                if (!isOutOfBoundries(p.c + 1, p.r + 1, energyMatrix)) {
                    if (newEnergy < distTo[p.r + 1][p.c + 1]) {
                        // bottom-right pixel
                        distTo[p.r + 1][p.c + 1] = newEnergy;
                        edgeTo[p.r + 1][p.c + 1] = p.c;
                        queue.enqueue(new Point(p.r + 1, p.c + 1));
                    }
                }
            }
        }

        // find the min in the bottom row
        // find the path with minimum energy
        double curMinSeamEnergy = Double.POSITIVE_INFINITY;
        for (int c = 0; c < width(); ++c) {
            curMinSeamEnergy = Math.min(curMinSeamEnergy, distTo[height() - 1][c]);
        }

        // get the index of min seam energy
        int minSeamIndex = -1;
        for (int c = 0; c < width(); ++c) {
            if (distTo[height() - 1][c] == curMinSeamEnergy) {
                minSeamIndex = c;
                break;
            }
        }

        // trace back
        int curRow = height() - 1;
        int nextIndex = minSeamIndex;
        seam[curRow] = minSeamIndex;
        while (curRow > 0) {
            seam[curRow - 1] = edgeTo[curRow][nextIndex];
            nextIndex = edgeTo[curRow][nextIndex];
            curRow--;
        }

        return seam;
    }


    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        this.colorMatrix = MatrixUtils.transposeMatrix(this.colorMatrix);
        int[] seam = findVerticalSeam();
        this.colorMatrix = MatrixUtils.transposeMatrix(this.colorMatrix);

        return seam;
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        if (seam == null) throw new IllegalArgumentException("null seam input.");
        if (seam.length != height()) throw new IllegalArgumentException("seam too long/short");
        if (width() <= 1) {
            throw new IllegalArgumentException("only have 0 or 1 pixel at width, cannot remove");
        }
        if (!validSeam(seam, width())) throw new IllegalArgumentException("Invalid seam.");

        // step 5
        // called with output of findVerticalSeam()
        // test with ResizeDemo
        final int beforeWidth = width();

        for (int r = 0; r < height(); ++r) {
            Color[] newRow = new Color[beforeWidth - 1];
            System.arraycopy(this.colorMatrix[r], 0, newRow, 0, seam[r]);
            System.arraycopy(this.colorMatrix[r], seam[r] + 1, newRow, seam[r], beforeWidth - seam[r] - 1); // length?
            this.colorMatrix[r] = newRow;
        }
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        if (seam == null) throw new IllegalArgumentException("null seam input.");
        if (seam.length != width()) throw new IllegalArgumentException("seam too long/short");
        if (height() <= 1) {
            throw new IllegalArgumentException("only have 0 or 1 pixel at height, cannot remove");
        }
        if (!validSeam(seam, height())) throw new IllegalArgumentException("Invalid seam.");

        // step 6
        // transpose, call remove vertical seam, transpose back
        this.colorMatrix = MatrixUtils.transposeMatrix(this.colorMatrix);
        removeVerticalSeam(seam);
        this.colorMatrix = MatrixUtils.transposeMatrix(this.colorMatrix);
    }

    private boolean validSeam(int[] seam, int max) {
        if (seam.length == 1) {
            return seam[0] == 0;
        }

        for (int i = 0; i < seam.length - 1; ++i) {
            if (seam[i] < 0 || seam[i] >= max) return false;

            if (Math.abs(seam[i] - seam[i + 1]) > 1) return false;
        }

        if (seam[seam.length - 1] < 0 || seam[seam.length - 1] >= max) return false;

        return true;
    }
}