package io.github.wuhao4u;

/**
 * Created by wuhao on 2017-06-06.
 */
public class DequeTest {
    public void addFirstTest() {
        Deque dq = new Deque();
        for(int i = 0; i < 10; ++i) {
            dq.addFirst(i);
        }

        System.out.println("Print dq:");
        dq.printDeque();
    }

    public void addLastTest() {
        Deque dq = new Deque();
        for(int i = 0; i < 2; ++i) {
            dq.addLast(i);
        }

        System.out.println("Print dq:");
        dq.printDeque();
    }

    public static void main(String[] args) {
        DequeTest dqTest = new DequeTest();

//        dqTest.addFirstTest();
        dqTest.addLastTest();
   }
}
