package io.github.wuhao4u;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by wuhao on 2017-06-10.
 */
public class FastCollinearPoints {
    private int segCount;
    private LineSegment[] segs;
    private HashMap<Point, Point> lineSegMap;

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
        if (points == null) throw new NullPointerException();
        else if (hasDuplicatedPoints(points)) {
            throw new IllegalArgumentException("Contains repeated points");
        } else if (points.length < 4) {
            throw new IllegalArgumentException("Less than 4 points.");
        }

        segCount = 0;
        segs = new LineSegment[8];
        lineSegMap = new HashMap<>();

        Point[] slopesToPoint = points.clone();
        for (int i = 0; i < points.length; ++i) {
            Arrays.sort(slopesToPoint, 0, slopesToPoint.length, points[i].slopeOrder());

            // find collinear (exclude itself)
//            System.out.printf("--- print slopes to %d point ---\n", i);
//            System.out.println(points[i]);
//            System.out.println("-------------------------------");

            int colCount = 0;
            for (int j = 0; j < slopesToPoint.length; ++j) {
                if (points[i].compareTo(slopesToPoint[j]) != 0) {
                    // this is not the point p itself
//                    System.out.println(points[i].slopeTo(slopesToPoint[j]));
//                    System.out.print("colCount: " + colCount + '\n');

                    if (points[i].slopeOrder().compare(slopesToPoint[j], slopesToPoint[j - 1]) == 0) {
                        // this point q1 and the previous point q2 has same slope to point p
                        // we might onto a collinear line
                        colCount++;

                        if (j == slopesToPoint.length - 1 && colCount >= 2) {
                            // this is the last one
                            if (segCount == segs.length) {
                                // double the segs size
                                segs = resizeSegments(2 * segs.length);
                            }

                            Point[] pointsInLine = Arrays.copyOfRange(slopesToPoint, j - colCount, j);

                            Arrays.sort(pointsInLine, 0, pointsInLine.length);

                            // if not already being added, then add this new segment
                            // OLDTODO: here maybe wrong. we want the corner ones
                            if (!isInSegments(pointsInLine[0], pointsInLine[pointsInLine.length - 1])) {
                                segs[segCount++] = new LineSegment(pointsInLine[0], pointsInLine[pointsInLine.length - 1]);
                                lineSegMap.put(pointsInLine[0], pointsInLine[pointsInLine.length - 1]);
                            }

                        }
                    }
                    else {
                        if (colCount >= 2) {
                            // we have more than 3 consecutive points
                            if (segCount == segs.length) {
                                // double the segs size
                                segs = resizeSegments(2 * segs.length);
                            }

                            Point[] pointsInLine = Arrays.copyOfRange(slopesToPoint, j - colCount, j);

                            Arrays.sort(pointsInLine, 0, pointsInLine.length);

                            // if not already being added, then add this new segment
                            // OLDTODO: here maybe wrong. we want the corner ones
                            segs[segCount++] = new LineSegment(pointsInLine[0], pointsInLine[pointsInLine.length - 1]);
                        }

                        // first point on its "line"
                        colCount = 0;
                    }
                }
            }
        }
    }

    private boolean isInSegments(Point p0, Point p1) {
        if (p0 == null || p1 == null) throw new NullPointerException("p0 or p1 is null.");

        if (lineSegMap.isEmpty() || lineSegMap == null) {
            return false;
        }

        if (lineSegMap.containsKey(p0) && lineSegMap.get(p0).compareTo(p1) == 0) {
            return true;
        } else if (lineSegMap.containsKey(p1) && lineSegMap.get(p1).compareTo(p0) == 0) {
            return true;
        } else {
            return false;
        }
    }

    private LineSegment[] resizeSegments(int capacity) {
        LineSegment[] nSegs = new LineSegment[capacity];
        for (int i = 0; i < segs.length; ++i) {
            nSegs[i] = segs[i];
        }
        return nSegs;
    }

    private boolean hasDuplicatedPoints(Point[] points) {
        for (int i = 0; i < points.length; ++i) {
            if (points == null) throw new NullPointerException("some point in the array is null.");
            for (int j = i + 1; j < points.length; ++j) {
                if (points[i].compareTo(points[j]) == 0) return true;
            }
        }
        return false;
    }

    // the number of line segments
    public int numberOfSegments() {
        return segCount;
    }

    // the line segments
    public LineSegment[] segments() {
        LineSegment[] ret = new LineSegment[segCount];
        for (int i = 0; i < segCount; ++i) {
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
