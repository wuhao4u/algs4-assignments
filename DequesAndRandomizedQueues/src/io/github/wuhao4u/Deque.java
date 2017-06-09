 package io.github.wuhao4u;

/**
 * Created by wuhao on 2017-06-06.
 */

import java.util.NoSuchElementException;
import java.util.Iterator;

public class Deque<Item> implements Iterable<Item> {
    private Node dummy; // sentinel head node
    private Node first; // top of the deque
    private Node last; // bottom of the deque
    private int n; // size of the deque

    private class Node {
        private Item item;
        private Node next;
        private Node prev;
    }

    // construct an empty deque
    // we don't really need to do anything here since we're using linked-list
    public Deque() {
        dummy = new Node();
        dummy.item = null;
        dummy.next = null;
        dummy.prev = null;

        first = null;
        last = null;

        n = 0;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return n == 0;
    }

    // return the number of items on the deque
    public int size() {
        return n;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) throw new NullPointerException("Item cannot be null");

        Node newNode = new Node();
        newNode.item = item;

        if (first == null) {
            // initiate the deque
            newNode.next = null;
            newNode.prev = dummy;
            dummy.next = newNode;

            first = newNode;
            last = newNode;
        } else if (this.size() == 1 && first == last) {
            // first & last pointing to the same node (the only node)
            newNode.next = first;
            newNode.prev = dummy;

            dummy.next = newNode;
            first.prev = newNode;

            first = newNode;
        } else {
            // there are more than one nodes exists in the list
            newNode.prev = dummy;
            newNode.next = first;

            dummy.next = newNode;
            first.prev = newNode;

            first = newNode;
        }
        n++;
    }

    // add the item to the end
    public void addLast(Item item) {
        if (item == null) throw new NullPointerException("Item cannot be null");

        Node newNode = new Node();
        newNode.item = item;

        if (this.isEmpty()) {
            // initialize the deque
            newNode.next = null;
            newNode.prev = dummy;
            last = newNode;
            first = newNode;
            dummy.next = newNode;
        } else {
            newNode.next = null;
            newNode.prev = last;
            last.next = newNode;
            last = last.next;
        }
        n++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (this.isEmpty()) throw new NoSuchElementException("The deque is empty.");

        Node oldFirst = first;
        first = first.next;
        oldFirst.next = null;

        if (first == null) {
            // we only had one node, now we're in an empty list
            last = null;
        } else {
            first.prev = dummy;
            dummy.next = first;
        }
        n--;
        return oldFirst.item;
    }

    // remove and return the item from the end
    public Item removeLast() {
        if (this.isEmpty()) throw new NoSuchElementException("The deque is empty.");

        Node oldLast = last;
        last = last.prev;
        last.next = null;

        n--;
        return oldLast.item;
    }

    // FOR TESTING
    /*
    public void printDeque() {
        Node current = dummy.next;

        while (current != null) {
            System.out.println(current.item);
            current = current.next;
        }
    }

    public void printDequeReversely() {
        Node current = last;
        while (current != dummy) {
            System.out.println(current.item);
            current = current.prev;
        }
    }
    */

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


    // check internal invariants
    private boolean check() {
        // check a few properties of instance variable 'first'
        if (n < 0) {
            return false;
        }
        if (n == 0) {
            if (first != null) return false;
            if (last != null) return false;
        } else if (n == 1) {
            if (first == null || last == null)
                return false;
            if (first.next != null || first.prev != null
                    || last.next != null || last.prev != null)
                return false;
        } else {
            if (first == null || last == null)
                return false;
            if (first.next == null || last.prev == null)
                return false;
        }

        // check internal consistency of instance variable n
        int numberOfNodes = 0;
        for (Node x = first; x != null && numberOfNodes <= n; x = x.next) {
            numberOfNodes++;
        }
        if (numberOfNodes != n) return false;

        numberOfNodes = 0;
        for (Node y = last; y != null && numberOfNodes <= n; y = y.prev) {
            numberOfNodes++;
        }
        if (numberOfNodes != n) return false;

        return true;
    }

    // unit testing (optional)
    public static void main(String[] args) {

    }
}
