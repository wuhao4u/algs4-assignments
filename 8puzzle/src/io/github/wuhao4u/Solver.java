package io.github.wuhao4u;

import com.sun.jmx.remote.internal.ArrayQueue;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Queue;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by wuhao on 2017-06-14.
 */
/*
TODO:
1. detect unsolvable puzzles
2. game tree
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

    private final SearchNode sol;
    private final MinPQ<SearchNode> openSet;
    private final ArrayList<SearchNode> closedSet;
    private final Board initialBoard;
    private final boolean solved;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) throw new NullPointerException("Missing initial board.");

        // initialize data structures
//        this.currentBoard = initial;
        this.initialBoard = initial;
        this.openSet = new MinPQ<>();
        this.closedSet = new ArrayList<>();
        this.sol = solve(initial);

        // if sol != null, meaning we got a solution
        this.solved = (sol != null);
    }

    private SearchNode solve(Board initial) {
        // start node
        SearchNode start = new SearchNode(initial, 0, initial.hamming(), null);
        openSet.insert(start);

        int moveMade = 1;
        while (!openSet.isEmpty()) {
            // if there are still nodes in openSet, process it

            SearchNode current = openSet.delMin();
            if (current.bd.isGoal()) {
                // we found the solution
                return current;
            }

            closedSet.add(current);

            for (Board neighbor : current.bd.neighbors()) {
                if (closedSet.contains(neighbor)) continue;

                if (!contains(openSet, neighbor)) {
                    SearchNode nNode = new SearchNode(neighbor, moveMade,
                            moveMade + neighbor.hamming(), current);
                    openSet.insert(nNode);
                }
            }

            moveMade++;
        }

        return null;
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
        // TODO: implement this. 2 puzzle instances
        return this.solved;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (isSolvable())
            return this.sol.gScore;
        else
            return -1;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (!isSolvable()) return null;

        // TODO: test solution needs to reconstruct the path
        ArrayDeque<Board> q = new ArrayDeque<>();

        SearchNode currentNode = this.sol;
        while (!currentNode.bd.equals(this.initialBoard)) {
            q.addFirst(currentNode.bd);
            currentNode = currentNode.cameFrom;
        }

        return q;
    }

    // solve a slider puzzle (given below)
    public static void main(String[] args) {
    }
}
