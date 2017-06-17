package io.github.wuhao4u;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.Stack;

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
        private SearchNode cameFrom;

        public SearchNode getCameFrom() {
            return cameFrom;
        }

        public SearchNode(Board initBoard, int gs, int fs, SearchNode cf) {
            this.bd = initBoard;
            this.gScore = gs;
            this.fScore = fs;
            this.cameFrom = cf;
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
    private final Stack<SearchNode> closedSet; // final node would be on the top of the stack
    private final Board initialBoard;
    private final boolean solvable;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) throw new NullPointerException("Missing initial board.");

        // initialize data structures
        this.initialBoard = initial;
        this.openSet = new MinPQ<>();
        this.closedSet = new Stack<>();

        this.solvable = aStar(initial);
    }

    private boolean aStar(Board initial) {
        // start node
        SearchNode start = new SearchNode(initial, 0, initial.hamming(), null);
        openSet.insert(start);

        // twin data structures
        MinPQ<SearchNode> twinOpen = new MinPQ<>();
        Stack<SearchNode> twinClosed = new Stack<>();

        // create a twin node with initial, for checking solvability
        Board twinInit = initial.twin();
        SearchNode twinStart = new SearchNode(twinInit, 0, twinInit.hamming(), null);
        twinOpen.insert(twinStart);

        while ((!openSet.isEmpty()) && (!twinOpen.isEmpty())) {
            // if there are still nodes in openSet, process it

            SearchNode current = openSet.delMin();
            closedSet.push(current);

            SearchNode twinCurrent = twinOpen.delMin();
            twinClosed.push(twinCurrent);

            if (current.bd.isGoal()) {
                // we found the solution
                return true;
            } else if (twinCurrent.bd.isGoal()) {
                return false;
            }

            for (Board neighbor : current.bd.neighbors()) {
                if (closedSet.search(neighbor) != -1) {
                    // we've already visited this board
                    continue;
                }

                if (!contains(openSet, neighbor)) {
                    SearchNode nNode = new SearchNode(neighbor, current.gScore + 1,
                            current.gScore + 1 + neighbor.hamming(), current);
                    openSet.insert(nNode);
                }
            }

            // twin
            for (Board twinNeighbor : twinCurrent.bd.neighbors()) {
                if (twinClosed.search(twinNeighbor) != -1) {
                    continue;
                }

                if (!contains(twinClosed, twinNeighbor)) {
                    SearchNode twinNewNode = new SearchNode(twinNeighbor, twinCurrent.gScore + 1,
                            twinCurrent.gScore + 1 + twinNeighbor.hamming(), twinCurrent);
                    twinOpen.insert(twinNewNode);
                }
            }
        }

        return false;
    }

    private boolean contains(Iterable<SearchNode> nodes, Board b) {
        for (SearchNode node : nodes) {
            // comparing board to board
            if (b.equals(node.bd)) return true;
        }

        return false;
    }

    // is the initial board solvable?
    public boolean isSolvable() {
        return this.solvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (isSolvable())
            return this.closedSet.peek().gScore;
        else
            return -1;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (!isSolvable()) return null;

        ArrayDeque<Board> q = new ArrayDeque<>();

        SearchNode currentNode = this.closedSet.peek();
        while (!currentNode.bd.equals(this.initialBoard)) {
            q.addFirst(currentNode.bd);
            currentNode = currentNode.cameFrom;
        }

        return q;
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
