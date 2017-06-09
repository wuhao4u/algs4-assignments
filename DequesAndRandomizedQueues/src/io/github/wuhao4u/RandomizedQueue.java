 package io.github.wuhao4u;

/**
 * Created by wuhao on 2017-06-06.
 */
import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdRandom;
/*
A randomized queue is similar to a stack or queue,
except that the item removed is chosen uniformly at random from items in the data structure.
 */
public class RandomizedQueue<Item> implements Iterable<Item> {
//    private boolean[] emptySlots, fullSlots; // each keep tracking the index of open/closed indices
    private int n;
    private Item[] a;

//    public Item[] getArray() {return a;}

    // construct an empty randomized queue
    public RandomizedQueue() {
        n = 0;
        a = (Item[]) new Object[4];
   }

    // is the queue empty?
    public boolean isEmpty() {
        return n == 0;
    }

    // return the number of items on the queue
    public int size() {
        return n;
    }

    private void resize(int capacity) {
        assert capacity >= n;

        // allocate new array (double sized)
        Item[] temp = (Item[]) new Object[capacity];

        // copy old cotent to the new one
        for (int i = 0; i < n; ++i) {
            temp[i] = a[i];
        }
        a = temp;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) throw new NullPointerException("Trying to add a null item to rand Queue.");

        if (n == a.length) {
            resize(2*a.length);
        }
        a[n] = item;
        n++;
    }


    private void swap(int i, int j) {
        Item temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) throw new NoSuchElementException("Stack underflow");

        // generate random index
        int i = StdRandom.uniform(n);

        swap(i, n-1);
        Item item = a[n-1];
        a[n-1] = null;                              // to avoid loitering
        n--;
        // shrink size of array if necessary
        if (n > 0 && n == a.length/4) resize(a.length/2);
        return item;
    }

    // return (but do not remove) a random item
    public Item sample() {
        if (isEmpty()) throw new NoSuchElementException("Stack underflow");
        int i = StdRandom.uniform(n);

        swap(i, n-1);
        return a[n-1];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new ArrayIterator();
    }

    private class ArrayIterator implements Iterator<Item> {
        private int current;
        private int[] sequence;

        public ArrayIterator() {
            current = 0;
            sequence = new int[a.length];
            for (int i = 0; i < sequence.length; ++i) {
                sequence[i] = i;
            }
            StdRandom.shuffle(sequence);
        }

        @Override
        public boolean hasNext() {
            return current < a.length;
        }

        @Override
        public Item next() {
            if (!this.hasNext()) {
                throw new NoSuchElementException(
                        "This is the end of the Randomized Queue.");
            }

            return a[sequence[current++]];
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException(
                    "Random queue does not have the remove method.");
        }
    }

    // unit testing (optional)
    public static void main(String[] args) {

    }
}
