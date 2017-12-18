import edu.princeton.cs.algs4.Picture;
import javafx.scene.input.PickResult;

public class SeamCarver {
    private final Picture picture;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture p) {
        this.picture = p;
    }

    // current picture
    public Picture picture() {

    }


    // width of current picture
    public int width() {

    }

    // height of current picture
    public int height() {

    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (x < 0 || x > this.picture.width() - 1) throw new IllegalArgumentException("Invalid x value");
        if (y < 0 || y > this.picture.height() - 1) throw new IllegalArgumentException("Invalid y value");

    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {

    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {

    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        if (seam == null) throw new IllegalArgumentException("null seam input.");
        if (seam.length != this.picture.width()) throw new IllegalArgumentException("seam too long/short");
        if (this.picture.height() <= 1) {
            throw new IllegalArgumentException("only have 0 or 1 pixel at height, cannot remove");
        }
        if (!validSeam(seam, this.picture.width())) throw new IllegalArgumentException("invalid seam");
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        if (seam == null) throw new IllegalArgumentException("null seam input.");
        if (seam.length != this.picture.height()) throw new IllegalArgumentException("seam too long/short");
        if (this.picture.width() <= 1) {
            throw new IllegalArgumentException("only have 0 or 1 pixel at width, cannot remove");
        }
        if (!validSeam(seam, this.picture.height())) throw new IllegalArgumentException("invalid seam");

    }

    private boolean validSeam(int[] seam, int max) {
        for(int i = 1; i < seam.length; ++i) {
            if (seam[i-1] < 0 || seam[i-1] > max - 1) return false;

            if(Math.abs(seam[i-1] - seam[i]) > 1) return false;
        }

        if (seam[seam.length-1] < 0 || seam[seam.length-1] > max - 1) return false;
        else return true;
    }
}