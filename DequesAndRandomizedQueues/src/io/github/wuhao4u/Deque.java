package io.github.wuhao4u;

/**
 * Created by wuhao on 2017-06-06.
 */

import java.util.NoSuchElementException;
import java.util.Iterator;

public class Deque<Item> implements Iterable<Item> {
    private Node first = null;
    private Node last = null;
    private int N = 0;

    private class Node {
        Item item;
        Node next;
        Node prev;
    }

    // construct an empty deque
    // we don't really need to do anything here since we're using linked-list
    public Deque() {

    }

    // is the deque empty?
    public boolean isEmpty() {
        return N == 0;
    }

    // return the number of items on the deque
    public int size() {
        return N;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) throw new NullPointerException("Item cannot be null");
    }

    // add the item to the end
    public void addLast(Item item) {
        if (item == null) throw new NullPointerException("Item cannot be null");
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (this.isEmpty()) throw new NoSuchElementException("The deque is empty.");

        Item oldFirst = first.item;
        first = first.next;
        return oldFirst;
    }

    // remove and return the item from the end
    public Item removeLast() {
        if (this.isEmpty()) throw new NoSuchElementException("The deque is empty.");

        Item oldLast = last.item;
        last = last.prev;
        return oldLast;
    }

    // return an iterator over items in order from front to end
    public Iterator<Item> iterator() {
        return new ListIterator();
    }

    private class ListIterator implements Iterator<Item> {
        private Node current = first;

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public Item next() {
            if (!this.hasNext()) throw new NoSuchElementException("This is the end of the deque.");

            Item item = current.item;
            current = current.next;
            return item;
        }

        public boolean hasPrevious() {
            return current != null;
        }

        public Item previous() {
            Item item = current.item;
            current = current.prev;
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
