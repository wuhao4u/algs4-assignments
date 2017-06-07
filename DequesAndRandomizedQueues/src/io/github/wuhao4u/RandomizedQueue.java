// TODO: comment out package info?
package io.github.wuhao4u;

/**
 * Created by wuhao on 2017-06-06.
 */
import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Node first = null;
    private Node last = null;
    private int N = 0;

    private class Node {
        Item item;
        Node next;
        Node prev;
    }

    // construct an empty randomized queue
    public RandomizedQueue() {}

    // is the queue empty?
    public boolean isEmpty() {
        return false;
    }

    // return the number of items on the queue
    public int size() {
        return 0;
    }

    // add the item
    public void enqueue(Item item) {

    }

    // remove and return a random item
//    public Item dequeue() {}

    // return (but do not remove) a random item
//    public Item sample() {}

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new ArrayIterator();
    }

    private class ArrayIterator implements Iterator<Item> {
        private Node current = first;

        @Override
        public boolean hasNext() {
            return false;
        }

        @Override
        public Item next() {
            if (!this.hasNext()) throw new NoSuchElementException("This is the end of the Randomized Queue.");

            Item item = current.item;
            current = current.next;
            return item;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Deque does not have the remove method. Sorry!");
        }
    }

    // unit testing (optional)
    public static void main(String[] args) {

    }
}
