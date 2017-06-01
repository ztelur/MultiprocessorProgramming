package com.company.nine;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by homer on 17-6-1.
 */
public class CoarseList<T> {
    private Node head;
    private Lock lock = new ReentrantLock();

    public CoarseList() {
        head = new Node(Integer.MIN_VALUE);
        head.next = new Node(Integer.MAX_VALUE);
    }

    public boolean add(T item) {
        Node pred, curr;
        int key = item.hashCode();
        lock.lock();
        try {
            pred = head;
            curr = pred.next;
            while(curr.key < key) {
                pred = curr;
                curr = pred.next;
            }
            if (key == curr.key) {
                return false;
            } else {
                Node node = new Node(item);
                node.next = curr;
                pred.next = node;
                return true;
            }

        } finally {
            lock.unlock();
        }
    }

    public boolean remove(T item) {
        Node pred,curr;
        lock.lock();
        try {
            pred = head;
            curr = head.next;
            int key = item.hashCode();
            while (curr.key < key) {
                pred = curr;
                curr = pred.next;
            }

            if (key == curr.key) {
                pred.next = curr.next;
                return true;
            } else {
                return false;
            }
        } finally {
            lock.unlock();
        }
    }


    private class Node {
        T item;
        int key;
        Node next;

        public Node(int key) {
            this.key = key;
        }

        public Node(T item) {
            this.item = item;
        }
    }
}


