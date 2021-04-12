package io.github.wuhao4u;

/**
 * Created by wuhao on 2017-06-07.
 */
public class RandomizedQueueTest {
    int testingSize = 4;

    private RandomizedQueue getRQ () {
        RandomizedQueue rq = new RandomizedQueue();
        for (int i = 0; i < testingSize; ++i) {
            rq.enqueue(i);
        }
        return rq;
    }

//    private void enqueueDequeueTest() {
//        RandomizedQueue rq = getRQ();
//
//        Object[] res = rq.getArray();
//        for (int i = 0; i < rq.size(); ++i) {
//            System.out.println(res[i]);
//        }
//
//        System.out.println("dequeue-----");
//        for (int i = 0; i < 5; ++i) {
//            System.out.println(rq.dequeue());
//        }
//    }

    private void sampleTest() {
        System.out.println("sample test---");
        RandomizedQueue rq = getRQ();

        for (int i = 0; i < testingSize * 3; ++i) {
            System.out.println(rq.sample());
        }
    }

    private void iteratorTest() {
        System.out.println("iterator test---");
        RandomizedQueue rq = getRQ();

        for (Object o :
                rq) {
            System.out.println(o);
        }

        for (Object o :
                rq) {
            System.out.println(o);
        }
    }

    public static void main(String[] args) {
        RandomizedQueueTest rqt = new RandomizedQueueTest();
//        rqt.enqueueDequeueTest();
//        rqt.sampleTest();
        rqt.iteratorTest();
    }
}
