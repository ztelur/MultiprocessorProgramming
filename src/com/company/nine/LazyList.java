package com.company.nine;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by homer on 17-6-6.
 */
public class LazyList<T> {
    Node head;

    public boolean add(T item) {
        int key = item.hashCode();
        while (true) {
            Node pred = head;
            Node curr = pred.next;

            while(curr.key < key) {
                pred = curr;
                curr = pred.next;
            }
            pred.lock();

            try {
                curr.lock();

                try {
                    if (validate(pred, curr)) {
                        if (curr.key == key) {
                            return false;
                        } else {
                            Node node = new Node(item);
                            node.next = curr;
                            pred.next = node;
                            return true;
                        }
                    }
                } finally {
                    curr.unlock();
                }

            } finally {
                pred.unlock();
            }
        }
    }

    private boolean validate(Node pred, Node curr) {
        return !pred.marked && !curr.marked && pred.next == curr;
    }

    public boolean remove(T item) {
        int key = item.hashCode();
        while(true) {
            Node pred = head;
            Node curr = pred.next;

            while (curr.key < key) {
                pred = curr;
                curr = pred.next;
            }
            pred.lock();
            try {
                curr.lock();
                try {
                    if (validate(pred, curr)) {
                        if (curr.key != key) {
                            return false;
                        } else {
                            curr.marked = true;
                            pred.next = curr.next;
                            return true;
                        }
                    }

                } finally {
                    curr.unlock();
                }

            } finally {
                pred.unlock();
            }
        }
    }

    public boolean contains(T item) {
        int key = item.hashCode();
        Node curr = head;
        while(curr.key < key) {
            curr = curr.next;
        }
        return curr.key == key && !curr.marked;
    }


    private class Node {
        T item;
        int key;
        boolean marked = false;
        Node next;
        Lock lock = new ReentrantLock();
        public Node(int key) {
            this.key = key;
        }

        public Node(T item) {
            this.item = item;
        }

        public void lock() {
            lock.lock();
        }

        public void unlock() {
            lock.unlock();
        }
    }
}
