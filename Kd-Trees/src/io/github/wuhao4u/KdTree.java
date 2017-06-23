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

        public Node(Point2D p, RectHV rect, Node ln, Node rn) {
            this.val = p;
            this.rect = rect;
            this.lb = ln;
            this.rt = rn;
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
        mRoot = put(mRoot, p, true, new RectHV(0, 0, 1, 1));
    }

    // x: parent node
    // p: the point of the node
    // hv: orientation of the new node
    private Node put(Node n, Point2D p, boolean isVertical, RectHV parentRect) {
        // root node has no parent node
        if (n == null) return new Node(p, parentRect, null, null);

        int cmp;
        RectHV rhv;
        if (isVertical) {
            // we determine by x value
            cmp = Double.compare(p.x(), n.val.x());
        } else {
            cmp = Double.compare(p.y(), n.val.y());
        }


        if (cmp < 0) {
            if (isVertical) {
                // set Xmax
                rhv = new RectHV(parentRect.xmin(), parentRect.ymin(), n.val.x(), parentRect.ymax());
            } else {

                // set Ymax
                rhv = new RectHV(parentRect.xmin(), parentRect.ymin(), parentRect.xmax(), n.val.y());
            }
            n.lb = put(n.lb, p, !isVertical, rhv);
        }
        if (cmp >= 0) {
            if (isVertical) {
                // set Xmin
                rhv = new RectHV(n.val.x(), parentRect.ymin(), parentRect.xmax(), parentRect.ymax());
            } else {
                // set Ymin
                rhv = new RectHV(parentRect.xmin(), n.val.y(), parentRect.xmax(), parentRect.ymax());
            }
            n.rt = put(n.rt, p, !isVertical, rhv);
        }

        // cmp == 0 meaning there's are duplicated points, should never happens,
        // if so, we'll just ignore it and keep the old one

        return n;
    }

    // draw all points to standard draw
    public void draw() {
        drawNode(mRoot, true);
    }

    private void drawNode(Node node, boolean isVertical) {
        if (node == null || node.val == null) return;

        // draw point itself
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        StdDraw.point(node.val.x(), node.val.y());

        StdDraw.setPenRadius(0.001);
        if (isVertical) {
            // draw vertical line, red
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(node.val.x(), node.rect.ymin(), node.val.x(), node.rect.ymax());
        } else {
            // draw horizontal line, blue
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(node.rect.xmin(), node.val.y(), node.rect.xmax(), node.val.y());
        }

        // left
        drawNode(node.lb, !isVertical);
        // right
        drawNode(node.rt, !isVertical);
    }

    // all points that are inside the rectangle
    public Iterable<Point2D> range(RectHV rect) {
        // TODO
        // if the query rectangle does not intersect the rectangle corresponding to a node,
        // there is no need to explore that node (or its subtrees).
        // A subtree is searched only if it might contain a point contained in the query rectangle.
        TreeSet ts = new TreeSet<Point2D>();

        range(mRoot, rect, ts);

        return ts;
    }

    private void range(Node node, RectHV rect, TreeSet<Point2D> set) {
        if (node == null || rect == null || set == null) return;

        if (!rect.intersects(node.rect)) return;

        if (rect.contains(node.val)) set.add(node.val);

        range(node.lb, rect, set);
        range(node.rt, rect, set);
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        // TODO
        // start at the root and recursively search in both subtrees
        Point2D nearestPoint = nearest(mRoot, p, null);

        return nearestPoint;
    }

    private Point2D nearest(Node node, Point2D target, Point2D closestSoFar) {
        if (target == null) throw new NullPointerException("missing target");
        if (node == null) return closestSoFar;
        if (closestSoFar == null) {
            closestSoFar = node.val;
        }

        // if the closest point discovered so far is closer than the distance between the query point
        // and the rectangle corresponding to a node, there is no need to explore that node (or its subtrees).

        if (target.distanceTo(closestSoFar) < target.distanceTo(node.val)) {
            // That is, a node is searched only if it might contain a point
            // that is closer than the best one found so far.
            return closestSoFar;
        } else if (target.distanceTo(node.val) < target.distanceTo(closestSoFar)) {
            // if current node's point is closer to the argument one, replace it
            closestSoFar = node.val;
        }

        // The effectiveness of the pruning rule depends on quickly finding a nearby point.
        // To do this, organize your recursive method so that when there are two possible subtrees to go down,
        // you always choose the subtree that is on the same side of the splitting line
        // as the query point as the first subtree to explore—the closest point found

        // while exploring the first subtree may enable pruning of the second subtree.

        if (node.lb != null && node.rt != null) {
            Point2D closeSideRes, farSideRes;
            if (target.distanceTo(node.lb.val) < target.distanceTo(node.rt.val)) {
                // left-below side is closer to the targeting point
                closeSideRes = nearest(node.lb, target, closestSoFar);
                farSideRes = nearest(node.rt, target, closestSoFar);
            } else {
                // right-top side is closer to the targeting point
                closeSideRes = nearest(node.rt, target, closestSoFar);
                farSideRes = nearest(node.lb, target, closestSoFar);
            }

            closestSoFar = (target.distanceTo(closeSideRes) < target.distanceTo(farSideRes)) ? closeSideRes : farSideRes;
        } else if (node.lb != null) {
            closestSoFar = nearest(node.lb, target, closestSoFar);
        } else if (node.rt != null) {
            closestSoFar = nearest(node.rt, target, closestSoFar);
        }

        return closestSoFar;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        String filename = args[0];
        In in = new In(filename);

        // initialize the data structures with N points from standard input
        KdTree kdtree = new KdTree();

        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            kdtree.insert(p);
        }

//        StdDraw.enableDoubleBuffering();
//        StdOut.println("---test draw---");
//        StdDraw.clear();
//        kdtree.draw();
//        StdDraw.show();
        Point2D p0, p05, p1;
        p0 = new Point2D(0, 0);
        p05 = new Point2D(0.5, 0.5);
        p1 = new Point2D(1., 1.);

        /*
        StdOut.println(kdtree.contains(p0));
        StdOut.println(kdtree.contains(p05));
        StdOut.println(kdtree.contains(p1));
        StdOut.println(kdtree.contains(new Point2D(0.851309, 0.881449)));
        StdOut.println(kdtree.contains(new Point2D(0.756544, 0.417366)));
        StdOut.println(kdtree.contains(new Point2D(0.785370, 0.652338)));
        StdOut.println(kdtree.contains(new Point2D(0.406360, 0.678100)));

        // test range
        StdOut.println("---test range---");
        RectHV leftHalf = new RectHV(0, 0, 0.50, 1);
        RectHV rightHalf = new RectHV(0.51, 0, 1, 1);

        TreeSet<Point2D> leftHalfPoints = (TreeSet<Point2D>) kdtree.range(leftHalf);
        TreeSet<Point2D> rightHalfPoints = (TreeSet<Point2D>) kdtree.range(rightHalf);

        StdOut.println("Number of points in the left half: " + leftHalfPoints.size());
        StdOut.println("Number of points in the right half: " + rightHalfPoints.size());
*/

        // test nearest
        StdOut.println("closest to point " + p0.toString() + "is point " + kdtree.nearest(p0).toString());
        StdOut.println("closest to point " + p05.toString() + "is point " + kdtree.nearest(p05).toString());
        StdOut.println("closest to point " + p1.toString() + "is point " + kdtree.nearest(p1).toString());
    }
}
