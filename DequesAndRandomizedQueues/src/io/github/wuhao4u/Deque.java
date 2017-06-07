// TODO: comment out package info?
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
    private int N; // size of the deque

    private class Node {
        Item item;
        Node next;
        Node prev;
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

        N = 0;
        assert check();
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

        Node newNode = new Node();
        newNode.item = item;

        if (first == null) {
            // initiate the deque
            newNode.next = null;
            newNode.prev = dummy;
            dummy.next = newNode;

            first = newNode;
            last = newNode;
       } else if (this.size() == 1 && first == last){
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
        N++;
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
        }
        else {
            newNode.next = null;
            newNode.prev = last;
            last.next = newNode;
            last = last.next;
        }
        N++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (this.isEmpty()) throw new NoSuchElementException("The deque is empty.");

        Item oldFirst = first.item;
        first = first.next;

        // TODO: doing here
        if (first == null) {
            // we're in an empty list
            last = dummy;
        }
        first.prev = dummy;
        dummy.next = first;

        return oldFirst;
    }

    // remove and return the item from the end
    public Item removeLast() {
        if (this.isEmpty()) throw new NoSuchElementException("The deque is empty.");

        Item oldLast = last.item;
        last = last.prev;
        return oldLast;
    }

    // TODO: delete me
    public void printDeque() {
        Node current = new Node();
        current = dummy.next;

        while (current != null) {
            System.out.println(current.item);
            current = current.next;
        }
    }

    public void printDequeReversely() {

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


    // check internal invariants
    private boolean check() {
        // check a few properties of instance variable 'first'
        if (N < 0) {
            return false;
        }
        if (N == 0) {
            if (first != null) return false;
            if (last != null) return false;
        }
        else if (N == 1) {
            if (first == null || last == null)
                return false;
            if (first.next != null || first.prev != null
                    || last.next != null || last.prev != null)
                return false;
        }
        else {
            if (first == null || last == null)
                return false;
            if (first.next == null || last.prev == null)
                return false;
        }

        // check internal consistency of instance variable n
        int numberOfNodes = 0;
        for (Node x = first; x != null && numberOfNodes <= N; x = x.next) {
            numberOfNodes++;
        }
        if (numberOfNodes != N) return false;

        numberOfNodes = 0;
        for (Node y = last; y != null && numberOfNodes <= N; y = y.prev) {
            numberOfNodes++;
        }
        if (numberOfNodes != N) return false;

        return true;
    }

    // unit testing (optional)
    public static void main(String[] args) {

    }
}
