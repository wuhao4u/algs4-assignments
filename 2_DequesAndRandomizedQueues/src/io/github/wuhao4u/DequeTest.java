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

        System.out.println("Print dq reversely:");
        dq.printDequeReversely();
    }

    public void addLastTest() {
        Deque dq = new Deque();
        for(int i = 0; i < 9; ++i) {
            dq.addLast(i);
        }

        System.out.println("Print dq:");
        dq.printDeque();

        System.out.println("Print dq reversely:");
        dq.printDequeReversely();
    }

    public void removeFirstTest() {
        Deque dq = new Deque();
        for (int i = 0; i < 5; i++) {
            dq.addFirst(i);
        }

        System.out.println("Print dq:");
        dq.printDeque();

        System.out.println("dq after remove first:");
        dq.removeFirst();
        dq.removeFirst();
        dq.printDeque();
        System.out.println("dq after remove first reversely:");
        dq.printDequeReversely();
    }

    public void removeLastTest() {
        Deque dq = new Deque();
        for (int i = 0; i < 5; i++) {
            dq.addFirst(i);
        }

        System.out.println("Print dq:");
        dq.printDeque();

        System.out.println("dq after remove last:");
        dq.removeLast();
        dq.removeLast();
        dq.printDeque();
        System.out.println("dq after remove last reversely:");
        dq.printDequeReversely();
    }

    public static void main(String[] args) {
        DequeTest dqTest = new DequeTest();

//        dqTest.addFirstTest();
//        dqTest.addLastTest();
//        dqTest.removeFirstTest();
        dqTest.removeLastTest();
   }
}
