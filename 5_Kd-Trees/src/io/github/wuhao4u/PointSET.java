package io.github.wuhao4u;

import edu.princeton.cs.algs4.*;

import java.util.TreeSet;

/**
 * Created by wuhao on 2017-06-20.
 */
public class PointSET {
    private TreeSet<Point2D> mSet;

    // construct an empty set of points
    public PointSET() {
        mSet = new TreeSet<>();
    }

    // is the set empty?
    public boolean isEmpty() {
        return size() == 0;
    }

    // number of points in the set
    public int size() {
        return mSet.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        // check if the p is in bound
        if (!inBound(p)) return;
        if (!mSet.contains(p)) mSet.add(p);
    }

    private boolean inBound(Point2D p) {
        if (Double.compare(p.x(), 0) < 0
                || Double.compare(p.x(), 1.0) > 0
                || Double.compare(p.y(), 0.0) < 0
                || Double.compare(p.y(), 1.0) > 0)
            return false;
        else return true;
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        return mSet.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        for (Point2D p : mSet) {
            StdDraw.point(p.x(), p.y());
        }
    }

    // all points that are inside the rectangle
    public Iterable<Point2D> range(RectHV rect) {
        // Naive implementation: compare each point with the 4 corners in rect
        TreeSet<Point2D> ts = new TreeSet<>();

        for (Point2D p : mSet) {
            if (p.y() <= rect.ymax() && p.y() >= rect.ymin()
                    && p.x() <= rect.xmax() && p.x() >= rect.xmin()) {
                ts.add(p);
            }
        }

        return ts;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        double currentMinDistance = (double) Integer.MAX_VALUE;
        Point2D currentMinPoint = new Point2D(-1., -1.);

        for (Point2D p0 : mSet) {
            if (p.distanceSquaredTo(p0) < currentMinDistance) {
                currentMinDistance = p.distanceSquaredTo(p0);
                currentMinPoint = p0;
            }
        }
        return currentMinPoint;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        /*
        // test insert(), isempty(), size()
        String filename = args[0];
        In in = new In(filename);


        // initialize the two data structures with point from standard input
        PointSET brute = new PointSET();
        KdTree kdtree = new KdTree();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            kdtree.insert(p);
            brute.insert(p);
        }

        StdOut.println("--- after insert ---");
        StdOut.printf("size of pSet: %d, is empty: %b\n", brute.size(), brute.isEmpty());

        // test contains()
        StdOut.println("---test contains---");
        StdOut.printf("contains 0,0? %b\n", brute.contains(new Point2D(0, 0)));
        StdOut.printf("contains 1,1? %b\n", brute.contains(new Point2D(1, 1)));
        StdOut.printf("contains random point exists? %b\n", brute.contains(new Point2D(0.851309, 0.881449)));
        StdOut.printf("contains random point? %b\n", brute.contains(new Point2D(0.5, 0.5)));

        StdDraw.enableDoubleBuffering();
        StdOut.println("---test draw---");
        StdDraw.clear();
        brute.draw();
        StdDraw.show();

        StdOut.println("---test range---");
        TreeSet<Point2D> rangePoints =
                (TreeSet<Point2D>) brute.range(new RectHV(0.25, 0.25, 0.5, 0.75));
        StdOut.printf("found %d points in the rectangle.\n", rangePoints.size());

        StdOut.println("---test nearest---");
        Point2D p0, p05, p1;
        p0 = new Point2D(0, 0);
        p05 = new Point2D(0.5, 0.5);
        p1 = new Point2D(1., 1.);

        Point2D np0, np05, np1;
        np0 = brute.nearest(p0);
        np05 = brute.nearest(p05);
        np1 = brute.nearest(p1);

        StdOut.printf("Nearest to the point 0,0 is (%f, %f)\n", np0.x(), np0.y());
        StdOut.printf("Nearest to the point center is (%f, %f)\n", np05.x(), np05.y());
        StdOut.printf("Nearest to the point 1,1 is (%f, %f)\n", np1.x(), np1.y());
        */
    }
}
