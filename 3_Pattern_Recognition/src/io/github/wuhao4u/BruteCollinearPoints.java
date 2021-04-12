package io.github.wuhao4u;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

/**
 * Created by wuhao on 2017-06-10.
 */
public class BruteCollinearPoints {
    private int segCount;
    private LineSegment[] segs;

    // the points in the argument has to be sorted
    private boolean hasDuplicatedPoints(Point[] points) {
        for (int i = 1; i < points.length; ++i) {
            if (points[i].equals(points[i-1]))
                return true;
        }
        return false;
    }

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        segCount = 0;
        segs = new LineSegment[8];
        Point[] pointsCopy = Arrays.copyOf(points, points.length);
        Arrays.sort(pointsCopy);

        if (points == null) throw new NullPointerException();
        if (hasDuplicatedPoints(pointsCopy)) {
            throw new IllegalArgumentException("Contains repeated points");
        }

        for (int i = 0; i < pointsCopy.length - 3; i++) {

            for (int j = i + 1; j < pointsCopy.length - 2; j++) {
                double s1 = pointsCopy[i].slopeTo(pointsCopy[j]);

                for (int k = j + 1; k < pointsCopy.length - 1; k++) {
                    double s2 = pointsCopy[i].slopeTo(pointsCopy[k]);

                    if (s1 == s2) {
                        for (int l = k + 1; l < pointsCopy.length; l++) {
                            double s3 = pointsCopy[i].slopeTo(pointsCopy[l]);

                            if (s1 == s3) {
                                if (segCount == segs.length) {
                                    // double the size of the segs
                                    LineSegment[] nSegs = new LineSegment[segs.length * 2];
                                    for(int p = 0; p < segs.length; ++p) {
                                        // copy old contents over to the new array
                                        nSegs[p] = segs[p];
                                    }
                                    segs = nSegs;
                                }

                                segs[segCount++] = new LineSegment(pointsCopy[i], pointsCopy[l]);
                            }
                        }
                    }
                }
            }
        }
    }

    // the number of line segments
    public int numberOfSegments() {
        return segCount;
    }

    // the line segments
    public LineSegment[] segments() {
        LineSegment[] ret = new LineSegment[segCount];
        for(int i = 0; i < segCount; ++i) {
            ret[i] = segs[i];
        }
        return ret;
    }

    public static void main(String[] args) {
        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);

        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
