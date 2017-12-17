import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;

import java.util.*;

public class SAP {
    Digraph G;

    // TODO: save known lcs (v,w) in a matrix? Memoize

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        this.G = G;
    }

    private boolean isValid(Digraph G, int v) {
        return v >= 0 && v <= G.V() - 1;
    }


    private boolean isValid(Digraph G, Iterable<Integer> vertices) {
        for (Integer v : vertices) {
            if (!isValid(G, v)) {
                return false;
            }
        }

        return true;
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        if (!isValid(G, v) || !isValid(G, w)) {
            throw new IllegalArgumentException("v or w is out of range.");
        }

        BreadthFirstDirectedPaths vPath = new BreadthFirstDirectedPaths(this.G, v);
        BreadthFirstDirectedPaths wPath = new BreadthFirstDirectedPaths(this.G, w);

        int lcs = ancestor(v, w);
        // we know the lowest common ancestor
        if (lcs != -1) {
            int vDist = vPath.distTo(lcs);
            int wDist = wPath.distTo(lcs);

            return vDist + wDist;
        }

        return -1;
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        if (!isValid(G, v) || !isValid(G, w)) {
            throw new IllegalArgumentException("v or w is out of range.");
        }

        BreadthFirstDirectedPaths vPath = new BreadthFirstDirectedPaths(this.G, v);
        BreadthFirstDirectedPaths wPath = new BreadthFirstDirectedPaths(this.G, w);

        int shortestLength = Integer.MAX_VALUE;
        int lcsDist = -1;
        int lcs = -1;
        for (int i = 0; i < G.V(); ++i) {
            if (vPath.hasPathTo(i) && wPath.hasPathTo(i)) {
//                int vToi = vPath.distTo(i);
//                int wToi  = wPath.distTo(i);

                lcsDist = vPath.distTo(i) + wPath.distTo(i);

                if (lcsDist < shortestLength) {
                    // this is the new lowest common ancestor
                    lcs = i;
                    shortestLength = lcsDist;
                }
            }
        }

        return lcs;
    }


    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    // v, w: path for all of belows?
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if (!isValid(G, v) || !isValid(G, w)) {
            throw new IllegalArgumentException("v or w is out of range.");
        }

        BreadthFirstDirectedPaths vPath = new BreadthFirstDirectedPaths(this.G, v);
        BreadthFirstDirectedPaths wPath = new BreadthFirstDirectedPaths(this.G, w);

        int lcs = ancestor(v, w);

        if (lcs == -1) {
            return -1;
        }

        if (vPath.hasPathTo(lcs) && wPath.hasPathTo(lcs)) {
            // shortest dist for a v to lcs
            int vToLCS = vPath.distTo(lcs);
            int wToLCS = wPath.distTo(lcs);

            return vToLCS + wToLCS;
        }

        return -1;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    // Purpose : find lcs out of many Vs and many Ws, and find the smallest lcs out of all
    // v, w: path from root to v or w
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (!isValid(G, v) || !isValid(G, w)) {
            throw new IllegalArgumentException("v or w is out of range.");
        }
        // low to high in the tree

        BreadthFirstDirectedPaths vPath = new BreadthFirstDirectedPaths(this.G, v);
        BreadthFirstDirectedPaths wPath = new BreadthFirstDirectedPaths(this.G, w);

        // adding all the ancestors into a queue for later selecting lowest
        Queue<Integer> ancestors = new LinkedList<Integer>();

        for (int i = 0; i < G.V(); ++i) {
            if (vPath.hasPathTo(i) && wPath.hasPathTo(i)) {
                ancestors.add(i);
            }
        }

        // selecting lowest common ancestor among all ancestors
        int shortestLen = Integer.MAX_VALUE;
        int currentLen = Integer.MAX_VALUE;
        int currentAncestor = -1;
        int lcs = -1;
        while (!ancestors.isEmpty()) {
            currentAncestor = ancestors.poll();
            currentLen = vPath.distTo(currentAncestor) + wPath.distTo(currentAncestor);

            if (currentLen < shortestLen) {
                shortestLen = currentLen;
                lcs = currentAncestor;
            }
        }

        return lcs;
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}
