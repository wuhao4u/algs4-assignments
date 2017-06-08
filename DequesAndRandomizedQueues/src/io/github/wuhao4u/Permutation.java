// package io.github.wuhao4u;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

/**
 * Created by wuhao on 2017-06-07.
 */
public class Permutation {
    public static void main(String[] args) {
        int k = Integer.parseInt(args[0]);
        RandomizedQueue<String> sequence = new RandomizedQueue<>();

        int counter = 0;
        while (!StdIn.isEmpty()) {
            String s = StdIn.readString();
            sequence.enqueue(s);
            counter++;
        }

        if (counter < k) throw new IllegalArgumentException("k is smaller than number of input strings");
        else {
            int j = 0;
            for (Object o : sequence) {
                StdOut.println(o);
                j++;
                if (j >= k) return;
            }
        }
    }
}
