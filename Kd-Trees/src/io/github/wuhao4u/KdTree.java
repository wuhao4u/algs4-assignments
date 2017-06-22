package io.github.wuhao4u;

import edu.princeton.cs.algs4.*;

import java.util.TreeSet;

/**
 * Created by wuhao on 2017-06-20.
 */
public class KdTree {
    private static class Node {
        private Point2D val;      // the point
        private RectHV rect;    // the axis-aligned rectangle corresponding to this node
        private Node lb;        // the left/bottom subtree
        private Node rt;        // the right/top subtree
        private double key;
//        private boolean vertical;

        public Node(Point2D p, RectHV rect, Node ln, Node rn) {
            this.val = p;
            this.rect = rect;
            this.lb = ln;
            this.rt = rn;
//            this.vertical = o;
        }
    }

    private Node mRoot;
    private int mSize;
    private static boolean mVertical; // flag for horizontal or vertical, true, 1 for vertical, false, 0 for horizontal

    // construct an empty set of points
    public KdTree() {
        mVertical = true; // 1st should be vertical
        mSize = 0;
    }

    // is the set empty?
    public boolean isEmpty() {
        return size() == 0;
    }

    // number of points in the set
    public int size() {
        return mSize;
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        Node n = get(mRoot, p, true);
        // TODO: test when the p does not exists
        return n != null;
    }

    private Node get(Node n, Point2D p, boolean isVertical) {
        // the base case, if the point does not exists in the tree
        if (n == null) return null;
        int cmp;

        if (isVertical) {
            cmp = Double.compare(p.x(), n.val.x());
        } else {
            cmp = Double.compare(p.y(), n.val.y());
        }
        if (cmp < 0) return get(n.lb, p, !isVertical);
        if (cmp > 0) return get(n.rt, p, !isVertical);
        else return n;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) throw new NullPointerException("Where is my point?");
        if (contains(p)) return;

        // alternate with x and y coordinates as p while inserting
        mRoot = put(mRoot, p, true);
        int x = 0;
    }

    // x: parent node
    // p: the point of the node
    // hv: orientation of the new node
    private Node put(Node n, Point2D p, boolean isVertical) {
        // TODO: step 2
        // root node has no parent node
        if (n == null) return new Node(p, null, null, null);

        int cmp;
        if (isVertical) {
            // we determine by x value
            cmp = Double.compare(p.x(), n.val.x());
        } else {
            cmp = Double.compare(p.y(), n.val.y());
//            cmp = Double.compare(n.val.y(), p.y());
        }

        if (cmp < 0) n.lb = put(n.lb, p, !isVertical);
        if (cmp > 0) n.rt = put(n.rt, p, !isVertical);
//        else n.val = p;

        return n;
    }


    // draw all points to standard draw
    public void draw() {

    }

    // all points that are inside the rectangle
    public Iterable<Point2D> range(RectHV rect) {
        TreeSet ts = new TreeSet<Point2D>();
        return ts;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        return p;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        String filename = args[0];
        In in = new In(filename);

//        StdDraw.enableDoubleBuffering();

        // initialize the data structures with N points from standard input
        KdTree kdtree = new KdTree();

        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            kdtree.insert(p);
        }

        Point2D p0, p05, p1;
        p0 = new Point2D(0, 0);
        p05 = new Point2D(0.5, 0.5);
        p1 = new Point2D(1., 1.);

        StdOut.println(kdtree.contains(p0));
        StdOut.println(kdtree.contains(p05));
        StdOut.println(kdtree.contains(p1));
        StdOut.println(kdtree.contains(new Point2D(0.851309, 0.881449)));
//        StdOut.println(kdtree.contains(new Point2D(0.147733,0.203388)));
    }
}
