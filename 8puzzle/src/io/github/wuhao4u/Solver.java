package io.github.wuhao4u;

import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Queue;

/**
 * Created by wuhao on 2017-06-14.
 */
/*
TODO:
1. detect unsolvable puzzles
2. game tree
 */

public final class Solver {
    private final Board tiles;
    private final MinPQ<SearchNode> openSet;
    private final MinPQ<SearchNode> closedSet;
    private final int movesMade;

    private final class SearchNode {
        // a board
        Board tiles;

        // the number of moves made to reach the board
        // cost from the start node to this node
        int gScore;

        // (optional?) cost from start to the goal (gScore + hamming/manhattan)
        // manhattan + moves
        int fScore;

        // previous search node
        SearchNode cameFrom;

        public SearchNode(Board initBoard, int gs, int fs, SearchNode cf) {
            this.tiles = initBoard;
            this.gScore = gs;
            this.fScore = fs;
            this.cameFrom = cf;
        }
    }

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) throw new NullPointerException("Missing initial board.");

        // initialize data structures
        this.tiles = initial;
        this.openSet = new MinPQ<>();
        this.closedSet = new MinPQ<>();
        this.movesMade = 0;

        SearchNode start = new SearchNode();
    }

    // is the initial board solvable?
    public boolean isSolvable() {
        return false;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (isSolvable())
            return movesMade;
        else
            return -1;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        Queue ret = new Queue();

        return ret;
    }

    // solve a slider puzzle (given below)
    public static void main(String[] args) {
    }
}
