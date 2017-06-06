package com.company.nine;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by homer on 17-6-6.
 */
public class OptimisticList<T> {
    private Node head;

    public boolean add(T item) {
        int key = item.hashCode();

        while (true) {
            Node pred = head;
            Node curr = pred.next;
            while (curr.key < key) {
                pred = curr;
                curr = pred.next;
            }
            pred.lock();
            curr.lock();
            try {
                if (validate(pred, curr)) {
                    if (curr.key == key) {
                        return false;
                    } else {
                        Node node = new Node(item);
                        node.next = curr;
                        pred.next = node;
                    }
                }
            } finally {
                pred.unlock();
                curr.unlock();
            }
        }
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
            curr.lock();

            try {
                if (validate(pred, curr)) {
                    if (curr.key == key) {
                        pred.next = curr.next;
                        return true;
                    } else {
                        return false;
                    }
                }
            } finally {
                pred.unlock();
                curr.unlock();
            }
        }
    }

    public boolean contains(T item) {
        int key = item.hashCode();
        while (true) {
            Node pred = this.head;
            Node curr = pred.next;
            while (curr.key < key) {
                pred = curr;
                curr = pred.next;
            }
            pred.lock();
            curr.lock();
            try {
                if (validate(pred, curr)) {
                    return (curr.key == key);
                }
            } finally {
                pred.unlock();
                curr.unlock();
            }
        }
    }

    public boolean validate(Node pred, Node curr) {
        Node node = head;
        while (node.key <= pred.key) {
            if (node == pred) {
                return pred.next == curr;
            }
            node = node.next;
        }
        return false;
    }

    private class Node {
        T item;
        int key;
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
