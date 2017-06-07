package io.github.wuhao4u;

/**
 * Created by wuhao on 2017-06-06.
 */
public class DequeTest {
    public void addFirstTest(Deque dq) {
        for(int i = 0; i < 5; ++i) {
            dq.addFirst(i);
        }

        System.out.println("Print dq:");
        dq.printDeque();
    }

    public static void main(String[] args) {
        Deque dq = new Deque();
        DequeTest dqTest = new DequeTest();

        dqTest.addFirstTest(dq);
   }
}
