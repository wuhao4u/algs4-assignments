package io.github.wuhao4u;

/**
 * Created by wuhao on 2017-06-15.
 */
public class TestBoard {
    private Board mBoard;

    public TestBoard(Board init) {
        mBoard = init;
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

    }
}
