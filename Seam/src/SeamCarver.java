import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.Queue;

import java.util.ArrayList;
import java.util.Arrays;
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

    // create a seam carver object based on the given picture
    public SeamCarver(final Picture p) {
        // making a hard copy of the original picture
        this.picture = new Picture(p);
    }

    // current picture
    public Picture picture() {
        return this.picture;
    }


    // width of current picture
    public int width() {
        return this.picture.width();
    }

    // height of current picture
    public int height() {
        return this.picture.height();
    }

    private boolean isOnBoundary(int c, int r) {
        if (c == 0 || c == this.picture.width() - 1) {
            return true;
        } else if (r == 0 || r == this.picture.height() - 1) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isOutOfBoundries(int c, int r) {
        if (r < 0 || r > this.picture.width() - 1) {
            return true;
        } else if (c < 0 || c > this.picture.height() - 1) {
            return true;
        } else {
            return false;
        }
    }

    private int deltaXSquare(int c, int r) {
        // c is [1,width]
        // r is [1,height]
        if (c <= 0 || c >= this.picture.width() - 1) throw new IllegalArgumentException("Invalid c value");
        if (r <= 0 || r >= this.picture.height() - 1) throw new IllegalArgumentException("Invalid r value");

        int rDiff = this.picture.get(c + 1, r).getRed() - this.picture.get(c - 1, r).getRed();
        int gDiff = this.picture.get(c + 1, r).getGreen() - this.picture.get(c - 1, r).getGreen();
        int bDiff = this.picture.get(c + 1, r).getBlue() - this.picture.get(c - 1, r).getBlue();

        return rDiff * rDiff + gDiff * gDiff + bDiff * bDiff;
    }

    private int deltaYSquare(int c, int r) {
        if (c < 0 || c > this.picture.width() - 1) throw new IllegalArgumentException("Invalid c value");
        if (r < 0 || r > this.picture.height() - 1) throw new IllegalArgumentException("Invalid r value");

        int rDiff = this.picture.get(c, r + 1).getRed() - this.picture.get(c, r - 1).getRed();
        int gDiff = this.picture.get(c, r + 1).getGreen() - this.picture.get(c, r - 1).getGreen();
        int bDiff = this.picture.get(c, r + 1).getBlue() - this.picture.get(c, r - 1).getBlue();

        return rDiff * rDiff + gDiff * gDiff + bDiff * bDiff;
    }

    // energy of pixel at column x and row y
    public double energy(int c, int r) {
        if (c < 0 || c > this.picture.width() - 1) throw new IllegalArgumentException("Invalid c value");
        if (r < 0 || r > this.picture.height() - 1) throw new IllegalArgumentException("Invalid r value");

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
                energyMatrix[r][c] = this.energy(r, c);
            }
        }

        return energyMatrix;
    }


    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        // TODO: step 3
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

        // fill the current energe to be the largest
        for (int r = 1; r < height(); ++r) {
            for (int c = 0; c < width(); ++c) {
                distTo[r][c] = Double.MAX_VALUE;
            }
        }

        Queue<Point> queue = new Queue<>();
        for (int c = 0; c < width(); ++c) {
            // try start from every entry in the top row

            queue.enqueue(new Point(0, c));
            // TODO: relax on the road

            while (!queue.isEmpty()) {
                Point p = queue.dequeue();

                // next pixel's energy will be sum(previous PXs) + E(x, y)
                double newEnergy = distTo[p.c][p.r] + this.energyMatrix[p.c][p.r];

                // try to relax below 3 pixels (x-1, y+1), (x, y+1), (x+1, y+1)
                if (!isOutOfBoundries(p.c - 1, p.r + 1) && (newEnergy < distTo[p.r + 1][p.c - 1])) {
                    // bottom-left pixel can be relaxed
                    distTo[p.r + 1][p.c - 1] = newEnergy;
                    edgeTo[p.r + 1][p.c - 1] = p.c;
                }

                if (!isOutOfBoundries(p.c, p.r + 1) && (newEnergy < distTo[p.r + 1][p.c])) {
                    // below pixel
                    distTo[p.r][p.c] = newEnergy;
                    edgeTo[p.r][p.c] = p.c;
                    queue.enqueue(new Point(p.r, p.c));
                }

                if (!isOutOfBoundries(p.c + 1, p.r + 1) && (newEnergy < distTo[p.r + 1][p.c + 1])) {
                    // bottom-right pixel
                    distTo[p.r + 1][p.c + 1] = newEnergy;
                    edgeTo[p.r + 1][p.c + 1] = p.c;
                    queue.enqueue(new Point(p.r + 1, p.c + 1));
                }
            }
        }


        // find the min in the bottom row
        // find the path with minimum energy
        double curMinSeamEnergy = Double.MAX_VALUE;
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
        int prevSeamIndex = edgeTo[height() - 1][minSeamIndex];
        int curRow = height() - 1;
        seam[curRow] = minSeamIndex; // bottom row's pixel

        while (curRow > 0) {
            // TODO: verify this logic
            seam[curRow - 1] = edgeTo[curRow][prevSeamIndex];
            prevSeamIndex = edgeTo[curRow - 1][prevSeamIndex];
            curRow--;
        }

        return seam;
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        // TODO: step 4
        // use findVerticalSeam(), with transposed images
        // left->right path with fewest energy

        return null;
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

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        if (seam == null) throw new IllegalArgumentException("null seam input.");
        if (seam.length != this.picture.height()) throw new IllegalArgumentException("seam too long/short");
        if (this.picture.width() <= 1) {
            throw new IllegalArgumentException("only have 0 or 1 pixel at width, cannot remove");
        }
        if (!validSeam(seam, this.picture.height())) throw new IllegalArgumentException("invalid seam");

        // TODO: step 5
        // called with output of findVerticalSeam()
        // test with ResizeDemo
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