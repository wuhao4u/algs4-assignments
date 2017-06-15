package io.github.wuhao4u;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

/**
 * Created by wuhao on 2017-06-10.
 */
public class FastCollinearPoints {
    private int segsCount;
    private LineSegment[] segs;

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
        segsCount = 0;
        segs = new LineSegment[8];
        Point[] slopesToPoint = points.clone();
        Arrays.sort(slopesToPoint);

        if (points == null) throw new NullPointerException();
        else if (hasDuplicatedPoints(slopesToPoint)) {
            throw new IllegalArgumentException("Contains repeated points");
        }


        /*
        for (int i = 0; i < points.length - 3; ++i) {
            Arrays.sort(slopesToPoint, points[i].slopeOrder());

            // find collinear (exclude itself)
            Point p = slopesToPoint[i];

            int first = 1;
            int last = 2;
            while (last < slopesToPoint.length) {
                while (last < slopesToPoint.length &&
                        Double.compare(p.slopeTo(slopesToPoint[first]), p.slopeTo(slopesToPoint[last])) == 0) {
                    last++;
                }

                if (last - first >= 3 && p.compareTo(slopesToPoint[first]) < 0) {

                    if (segsCount == segs.length) {
                        segs = resizeSegments(segs.length * 2);
                    }
                    segs[segsCount++] = new LineSegment(p, slopesToPoint[last]);
                }

                first = last;
                last++;
            }
        }
        */
        for (int i = 0; i < slopesToPoint.length - 3; i++) {
            Arrays.sort(slopesToPoint);

            // Sort the points according to the slopes they makes with p.
            // Check if any 3 (or more) adjacent points in the sorted order
            // have equal slopes with respect to p. If so, these points,
            // together with p, are collinear.

            Arrays.sort(slopesToPoint, slopesToPoint[i].slopeOrder());

            for (int p = 0, first = 1, last = 2; last < slopesToPoint.length; last++) {
                // find last collinear to p point
                while (last < slopesToPoint.length
                        && Double.compare(slopesToPoint[p].slopeTo(slopesToPoint[first]), slopesToPoint[p].slopeTo(slopesToPoint[last])) == 0) {
                    last++;
                }
                // if found at least 3 elements, make segment if it's unique
                if (last - first >= 3 && slopesToPoint[p].compareTo(slopesToPoint[first]) < 0) {// this is brilliant!!!!
                    if (segsCount == segs.length) {
                        segs = resizeSegments(segs.length * 2);
                    }
                    segs[segsCount++] = new LineSegment(slopesToPoint[p], slopesToPoint[last - 1]);
                }
                // Try to find next
                first = last;
            }
        }
    }

    private LineSegment[] resizeSegments(int capacity) {
        LineSegment[] nSegs = new LineSegment[capacity];
        for (int i = 0; i < segs.length; ++i) {
            nSegs[i] = segs[i];
        }
        return nSegs;
    }

    // the points needs to be sorted
    private boolean hasDuplicatedPoints(Point[] points) {
        if (points.length < 2) return false;

        for (int i = 1; i < points.length; ++i) {
            if (points[i].equals(points[i - 1])) return true;
        }
        return false;
    }

    // the number of line segments
    public int numberOfSegments() {
        return segsCount;
    }

    // the line segments
    public LineSegment[] segments() {
        LineSegment[] ret = new LineSegment[segsCount];
        for (int i = 0; i < segsCount; ++i) {
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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        LineSegment[] ls = collinear.segments();

        for (LineSegment segment : ls) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
