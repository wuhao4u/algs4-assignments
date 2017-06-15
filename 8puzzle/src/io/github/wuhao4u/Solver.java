package io.github.wuhao4u;

import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Queue;

/**
 * Created by wuhao on 2017-06-14.
 */
public final class Solver {
    private final Board tiles;
    private final MinPQ<SearchNode> sol;
    private int movesMade;

    private final class SearchNode {
        Board b;
        int moveMade;
        SearchNode prev;
    }

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) throw new NullPointerException("Missing initial board.");
        this.tiles = initial;
        this.sol = new MinPQ<>();
    }

    // is the initial board solvable?
    public boolean isSolvable() {
        return false;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return -1;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        Queue ret = new Queue();

        return ret;
    }

    // solve a slider puzzle (given below)
    public static void main(String[] args) {}
}
