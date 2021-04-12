package io.github.wuhao4u;

/**
 * Created by wuhao on 2017-06-15.
 */
public class TestBoard {
    private Board mBoard, solBoard, oriBoard;

    public TestBoard(Board init) {
        mBoard = init;
        int[][] solTiles = {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 0}
        };
        solBoard = new Board(solTiles);

        int[][] oriTiles = {
                {0, 1, 3},
                {4, 2, 5},
                {7, 8, 6}
        };
        oriBoard = new Board(oriTiles);
    }

    public void testHamming() {
        System.out.println("hamming:");
        System.out.println(mBoard.hamming());
    }

    public void testManhattan() {
        System.out.println("manhattan:");
        System.out.println(mBoard.manhattan());
    }

    public void testIsGoal() {
        int[][] sol = {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 0}
        };

        Board puzzle4 = new Board(sol);
        System.out.println(puzzle4.isGoal());
        assert puzzle4.isGoal();
    }

    public void testTwin() {
        System.out.println("---before twin---");
        System.out.println(mBoard);

        for (int i = 0; i < 50; i++) {
            System.out.println("---after twin---");
            System.out.println(mBoard.twin());
        }
    }

    public void testEquals() {
        assert !mBoard.equals(solBoard);
        assert mBoard.equals(oriBoard);
    }

    public void testNeighbors() {
        Iterable<Board> neighbors = mBoard.neighbors();
        for(Board b : neighbors) {
            System.out.println("------");
            System.out.println(b.toString());
        }
    }
}
