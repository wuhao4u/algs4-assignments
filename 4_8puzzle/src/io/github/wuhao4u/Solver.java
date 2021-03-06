package io.github.wuhao4u;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayDeque;
import java.util.Comparator;

/**
 * Created by wuhao on 2017-06-14.
 */
/*
2. game tree?
 */

public final class Solver {
    private final class SearchNode implements Comparable<SearchNode>, Comparator<SearchNode> {
        // a board
        private Board bd;

        public Board getTiles() {
            return bd;
        }

        // the number of moves made to reach the board
        // cost from the start node to this node
        private int gScore;

        public int getgScore() {
            return gScore;
        }

        // (optional?) cost from start to the goal (gScore + hamming/manhattan)
        // manhattan + moves
        private int fScore;

        public int getfScore() {
            return fScore;
        }

        // previous search node
        private SearchNode prev;

        public SearchNode getCameFrom() {
            return prev;
        }

        public SearchNode(Board initBoard, int gs, int fs, SearchNode cf) {
            this.bd = initBoard;
            this.gScore = gs;
            this.fScore = fs;
            this.prev = cf;
        }

        @Override
        public int compareTo(SearchNode that) {
            return this.fScore - that.fScore;
        }

        @Override
        public int compare(SearchNode n1, SearchNode n2) {
            return n1.fScore - n2.fScore;
        }
    }

    private final MinPQ<SearchNode> openSet;
    //    private final Stack<SearchNode> closedSet; // final node would be on the top of the stack
    private final Board initialBoard;
    private final boolean solvable;
    private final SearchNode lastNode;
    private final ArrayDeque<Board> solutionPath;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) throw new NullPointerException("Missing initial board.");

        // initialize data structures
        this.initialBoard = initial;
        this.openSet = new MinPQ<>();

        this.lastNode = aStar(initial);

        if (lastNode == null) throw new NullPointerException();

        this.solutionPath = new ArrayDeque<>();

        SearchNode tracer = lastNode;
        while (tracer.prev != null) {
            this.solutionPath.addFirst(tracer.bd);
            tracer = tracer.prev;
        }
        // the initial node
        solutionPath.addFirst(tracer.bd);

        this.solvable = (tracer.bd.equals(initial));
    }

    private SearchNode aStar(Board initial) {
        // start node
        SearchNode start = new SearchNode(initial, 0, initial.manhattan(), null);
        openSet.insert(start);

        // create a twin node with initial, for checking solvability
        Board twinInit = initial.twin();
        SearchNode twinStart = new SearchNode(twinInit, 0, twinInit.manhattan(), null);
        openSet.insert(twinStart);

        SearchNode current;
        while (true) {
            current = openSet.delMin();

            if (current.bd.isGoal()) break;

            for (Board neighbor : current.bd.neighbors()) {
                if (current.prev == null || !neighbor.equals(current.prev.bd)) {
                    // null meaning is the init node, neighbor should not be the node that it came from
                    SearchNode nNode = new SearchNode(neighbor, current.gScore + 1,
                            current.gScore + 1 + neighbor.manhattan(), current);
                    openSet.insert(nNode);
                }
            }
            // if there are still nodes in openSet, process it
//            current = openSet.delMin();
        }

        return current;
    }

    // is the initial board solvable?
    public boolean isSolvable() {
        return this.solvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (isSolvable())
            return this.lastNode.gScore;
        else
            return -1;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (!isSolvable())
            return null;
        else
            return this.solutionPath;
    }

    // solve a slider puzzle (given below)
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
