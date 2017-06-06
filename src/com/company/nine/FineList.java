package com.company.nine;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by homer on 17-6-1.
 */
public class FineList<T> {
    private Node head;

    public boolean add(T item) {
        int key = item.hashCode();
        head.lock();
        Node pred = head;
        try {
            Node curr = pred.next;
            curr.lock();

            try {
                while (curr.key < key) {
                    pred.unlock();
                    pred = curr;
                    curr = pred.next;
                    curr.lock();
                }

                if (curr.key == key) {
                    return false;
                }
                Node newNode = new Node(item);
                newNode.next = curr;
                pred.next = newNode;
                return true;
            } finally {
                curr.unlock();
            }

        } finally {
            pred.unlock();
        }
    }

    public boolean remove(T item) {
        int key = item.hashCode();
        Node pred = head;
        pred.lock();
        try {
            Node curr = pred.next;
            curr.lock();
            try {
                while (curr.key < key) {
                    pred.unlock();
                    pred = curr;
                    curr = pred.next;
                    curr.lock();
                }

                if (curr.key == key) {
                    pred.next = curr.next;
                    return true;
                }
                return false;
            } finally {
                curr.unlock();
            }
        } finally {
            pred.unlock();
        }
    }

    public boolean contains(T item) {
        int key = item.hashCode();
        Node pred = head;
        pred.lock();
        try {
            Node curr = pred.next;
            curr.lock();
            try {
                while (curr.key < key) {
                    pred.unlock();
                    pred = curr;
                    curr = pred.next;
                    curr.lock();
                }

                if (curr.key == key) {
                    return true;
                }
                return false;
            } finally {
                curr.unlock();
            }
        } finally {
            pred.unlock();
        }
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
