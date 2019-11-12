import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.Queue;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class SeamCarver {
    class Point {
        int r, c;

        public Point(int r, int c) {
            this.r = r;
            this.c = c;
        }
    }

    private static final double BOUNDARY_ENERGY = 1000.0;
    private Picture picture;
    private double[][] energyMatrix;
    // TODO: keep track of the current matrix orientation, convert only when it's needed
    private boolean isMatrixVertical;
    private Color[][] colorMatrix;

    // create a seam carver object based on the given picture
    public SeamCarver(final Picture p) {
        // making a hard copy of the original picture
        this.picture = new Picture(p);
        this.isMatrixVertical = true;

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
                p.set(r, c, this.colorMatrix[r][c]);
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
        if (c == 0 || c == width() - 1) {
            return true;
        } else if (r == 0 || r == height() - 1) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isOutOfBoundries(int c, int r, double[][] matrix) {
        if (r < 0 || r > matrix.length - 1) {
            return true;
        } else if (c < 0 || c > matrix[0].length - 1) {
            return true;
        } else {
            return false;
        }
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
        if (c < 0 || c > this.picture.width() - 1) throw new IllegalArgumentException("Invalid c value");
        if (r < 0 || r > this.picture.height() - 1) throw new IllegalArgumentException("Invalid r value");

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
        double[][] energyMatrix = new double[height()][width()];

        for (int r = 0; r < height(); ++r) {
            for (int c = 0; c < width(); ++c) {
                energyMatrix[r][c] = energy(c, r);
            }
        }

        return energyMatrix;
    }

    private int[] findSeam(double[][] energyMatrix) {
        final int emHeight = energyMatrix.length;
        final int emWidth = energyMatrix[0].length;

        // the return result
        int[] seam = new int[emHeight];

        // lowest total energy so far
        // 1st row is always 1000
        double[][] distTo = new double[emHeight][emWidth];
        Arrays.fill(distTo[0], 0);

        // previous row's index for the result seam, for back trace in the end
        int[][] edgeTo = new int[emHeight][emWidth];
        Arrays.fill(edgeTo[0], -1);

        // fill the current energy to be the largest
        for (int r = 1; r < emHeight; ++r) {
            for (int c = 0; c < emWidth; ++c) {
                distTo[r][c] = Double.MAX_VALUE;
            }
        }

        Queue<Point> queue = new Queue<>();
        for (int c = 0; c < emWidth; ++c) {
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
        double curMinSeamEnergy = Double.MAX_VALUE;
        for (int c = 0; c < emWidth; ++c) {
            curMinSeamEnergy = Math.min(curMinSeamEnergy, distTo[emHeight - 1][c]);
        }

        // get the index of min seam energy
        int minSeamIndex = -1;
        for (int c = 0; c < emWidth; ++c) {
            if (distTo[emHeight - 1][c] == curMinSeamEnergy) {
                minSeamIndex = c;
                break;
            }
        }

        // trace back
        int curRow = emHeight - 1;
        int nextIndex = minSeamIndex;
        seam[curRow] = minSeamIndex;
        while (curRow > 0) {
            seam[curRow - 1] = edgeTo[curRow][nextIndex];
            nextIndex = edgeTo[curRow][nextIndex];
            curRow--;
        }

        return seam;
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        // construct a 2d energy array using energy() method
        // use PrintSeams to test code
        // top->down path with fewest energy

        /*
        To write findVerticalSeam(), you will want to first make sure you understand the topologial sort algorithm
        for computing a shortest path in a DAG. Do not create an EdgeWeightedDigraph.
        Instead construct a 2d energy array using the energy() method that you have already written.
        Your algorithm can traverse this matrix treating some select entries as reachable
        from (x, y) to calculate where the seam is located. To test that your code works,
        use the client PrintSeams described in the testing section above.
         */
        // TODO: the calling to regenerate energy matrix can be reduced

        if (!this.isMatrixVertical) {
            // energy matrix is now vertical
            this.energyMatrix = MatrixUtils.transposeMatrix(this.energyMatrix);
            this.isMatrixVertical = true;
        }

        int[] seam = findSeam(this.energyMatrix);

        return seam;
    }


    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        // use findVerticalSeam(), with transposed images
        // left->right path with fewest energy
        // transpose image

        if (this.isMatrixVertical) {
            // energy matrix is not horizontal
            this.energyMatrix = MatrixUtils.transposeMatrix(this.energyMatrix);
            this.isMatrixVertical = false;
        }

        int[] seam = findSeam(this.energyMatrix);

        return seam;
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        if (seam == null) throw new IllegalArgumentException("null seam input.");
        if (seam.length != this.picture.height()) throw new IllegalArgumentException("seam too long/short");
        if (this.picture.width() <= 1) {
            throw new IllegalArgumentException("only have 0 or 1 pixel at width, cannot remove");
        }
        if (!validSeam(seam, this.picture.height())) throw new IllegalArgumentException("invalid seam");

        // TODO: step 5
        // TODO: re-calculate energy matrix only needed
        // called with output of findVerticalSeam()
        // test with ResizeDemo
        if (!this.isMatrixVertical) {
            this.energyMatrix = MatrixUtils.transposeMatrix(this.energyMatrix);
            this.isMatrixVertical = true;
        }

        final int beforeHeight = height();
        final int beforeWidth = width();

        for (int r = 0; r < height(); ++r) {
            Color[] beforeRow = this.colorMatrix[r];
            System.arraycopy(beforeRow, 0, this.colorMatrix[r], 0, seam[r]);
            System.arraycopy(this.colorMatrix[r], seam[r] + 1, this.colorMatrix[r], 0, seam[r]);
        }
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        if (seam == null) throw new IllegalArgumentException("null seam input.");
        if (seam.length != this.picture.width()) throw new IllegalArgumentException("seam too long/short");
        if (this.picture.height() <= 1) {
            throw new IllegalArgumentException("only have 0 or 1 pixel at height, cannot remove");
        }
        if (!validSeam(seam, this.picture.width())) throw new IllegalArgumentException("invalid seam");

        // TODO: step 6
        // transpose, call remove vertical seam, transpose back
    }


    private boolean validSeam(int[] seam, int max) {
        for (int i = 1; i < seam.length; ++i) {
            if (seam[i - 1] < 0 || seam[i - 1] > max - 1) return false;

            if (Math.abs(seam[i - 1] - seam[i]) > 1) return false;
        }

        if (seam[seam.length - 1] < 0 || seam[seam.length - 1] > max - 1) return false;
        else return true;
    }
}