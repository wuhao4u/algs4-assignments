package io.github.wuhao4u;

import com.sun.javaws.exceptions.InvalidArgumentException;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import java.lang.reflect.Array;

/**
 * Created by wuhao on 2017-06-10.
 */
public class BruteCollinearPoints {
    private int segCount;
    private LineSegment[] segs;

    private boolean hasDuplicatedPoints(Point[] points) {
        for (int i = 0; i < points.length; ++i) {
            for (int j = i + 1; j < points.length; ++j) {
                if (points[i].compareTo(points[j]) == 0) return true;
            }
        }
        return false;
    }

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        if (points == null) throw new NullPointerException();
        else if (hasDuplicatedPoints(points)) {
            throw new IllegalArgumentException("Contains repeated points");
        } else if (points.length < 4) {
            throw new IllegalArgumentException("Less than 4 points.");
        }

        segCount = 0;
        segs = new LineSegment[8];

        for (int i = 0; i < points.length; ++i) {
            for (int j = i + 1; j < points.length; ++j) {
                double s1 = points[i].slopeTo(points[j]);

                for (int k = j + 1; k < points.length; ++k) {
                    double s2 = points[j].slopeTo(points[k]);

                    if (s1 == s2) {
                        // the Pi Pj, Pj Pk has the same slope
                        for (int l = k + 1; l < points.length; ++l) {
                            double s3 = points[k].slopeTo(points[l]);
                            if (s2 == s3) {

                                // dynamic array
                                if (segCount == segs.length) {
                                    LineSegment[] newSegs = new LineSegment[segs.length * 2];

                                    for (int x = 0; x < segs.length; ++x) {
                                        newSegs[x] = segs[x];
                                    }
                                    segs = newSegs;
                                }
                                segs[segCount++] = new LineSegment(points[i], points[l]);
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
