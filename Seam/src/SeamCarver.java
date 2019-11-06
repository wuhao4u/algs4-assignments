import edu.princeton.cs.algs4.Picture;

public class SeamCarver {
    private final static double BOUNDARY_ENERGY = 1000.0;
    private final Picture picture;
    private double[][] energyMatrix;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture p) {
        this.picture = p;
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

    private boolean isOnBoundary(int x , int y) {
        if (x == 0 || x == this.picture.width() - 1) {
            return true;
        } else if (y == 0 || y == this.picture.height() -1) {
            return true;
        } else {
            return false;
        }
    }

    private int deltaXSquare(int x, int y) {
        // x is [1,width]
        // y is [1,height]
        if (x <= 0 || x >= this.picture.width() - 1) throw new IllegalArgumentException("Invalid x value");
        if (y <= 0 || y >= this.picture.height() - 1) throw new IllegalArgumentException("Invalid y value");

        int rDiff = this.picture.get(x+1, y).getRed() - this.picture.get(x-1, y).getRed();
        int gDiff = this.picture.get(x+1, y).getGreen() - this.picture.get(x-1, y).getGreen();
        int bDiff = this.picture.get(x+1, y).getBlue() - this.picture.get(x-1, y).getBlue();

        return rDiff * rDiff + gDiff * gDiff + bDiff * bDiff;
    }

    private int deltaYSquare(int x, int y) {
        if (x < 0 || x > this.picture.width() - 1) throw new IllegalArgumentException("Invalid x value");
        if (y < 0 || y > this.picture.height() - 1) throw new IllegalArgumentException("Invalid y value");

        int rDiff = this.picture.get(x, y+1).getRed() - this.picture.get(x, y-1).getRed();
        int gDiff = this.picture.get(x, y+1).getGreen() - this.picture.get(x, y-1).getGreen();
        int bDiff = this.picture.get(x, y+1).getBlue() - this.picture.get(x, y-1).getBlue();

        return rDiff * rDiff + gDiff * gDiff + bDiff * bDiff;
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (x < 0 || x > this.picture.width() - 1) throw new IllegalArgumentException("Invalid x value");
        if (y < 0 || y > this.picture.height() - 1) throw new IllegalArgumentException("Invalid y value");

        if (isOnBoundary(x, y)) {
            return BOUNDARY_ENERGY;
        } else {
            return Math.sqrt(deltaXSquare(x, y) + deltaYSquare(x, y));
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

    private int[] shortestVerticalPath(double[][] energyMatrix, int startCol) {
        /*
        We initialize distances to all vertices as infinite and distance to source as 0, then we find a topological sorting of the graph.
        Topological Sorting of a graph represents a linear ordering of the graph (See below, figure (b) is a linear representation of figure (a) ).
        Once we have topological order (or linear representation), we one by one process all vertices in topological order. For every vertex being processed,
        we update distances of its adjacent using distance of current vertex.
         */
        double[][] distTo = new double[width()][height()];
        for (int r = 0; r < height(); ++r) {
            for (int c = 0; c < width(); ++c) {
                distTo[r][c] = Double.MAX_VALUE;
            }
        }
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

        // find the path with minimum energy
        double curMinSeam = Double.MAX_VALUE;
        for (int c = 0; c < width(); ++c) {
            // try start from every entry in the top row
            shortestVerticalPath(this.energyMatrix, c);
        }

        return null;
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