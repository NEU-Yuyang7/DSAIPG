package com.phasmidsoftware.dsaipg.adt.pq;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Random;

class FibonacciHeapNode<T> {
    T data;
    double key;
    FibonacciHeapNode<T> parent, child, left, right;
    int degree;
    boolean mark;

    public FibonacciHeapNode(T data, double key) {
        this.data = data;
        this.key = key;
        this.right = this;
        this.left = this;
    }
}

class FibonacciHeap<T> {
    private FibonacciHeapNode<T> minNode;
    private int size;
    private HashMap<Integer, FibonacciHeapNode<T>> degreeTable = new HashMap<>();

    public void insert(FibonacciHeapNode<T> node) {
        if (minNode == null) {
            minNode = node;
        } else {
            mergeLists(minNode, node);
            if (node.key < minNode.key) {
                minNode = node;
            }
        }
        size++;
    }

    public FibonacciHeapNode<T> extractMin() {
        FibonacciHeapNode<T> min = minNode;
        if (min != null) {
            if (min.child != null) {
                FibonacciHeapNode<T> child = min.child;
                do {
                    FibonacciHeapNode<T> next = child.right;
                    mergeLists(min, child);
                    child.parent = null;
                    child = next;
                } while (child != min.child);
            }
            removeNode(min);
            if (min == min.right) {
                minNode = null;
            } else {
                minNode = min.right;
                consolidate();
            }
            size--;
        }
        return min;
    }

    public FibonacciHeapNode<T> findMin() {
        return minNode;
    }

    public int size() {
        return size;
    }

    private void mergeLists(FibonacciHeapNode<T> a, FibonacciHeapNode<T> b) {
        if (a == null || b == null) return;
        a.right.left = b.left;
        b.left.right = a.right;
        a.right = b;
        b.left = a;
    }

    private void removeNode(FibonacciHeapNode<T> node) {
        node.left.right = node.right;
        node.right.left = node.left;
    }

    private void consolidate() {
        HashMap<Integer, FibonacciHeapNode<T>> degreeTable = new HashMap<>();
        FibonacciHeapNode<T> start = minNode;
        FibonacciHeapNode<T> current = minNode;
        if (current == null) return;
        
        do {
            FibonacciHeapNode<T> x = current;
            int d = x.degree;
            while (degreeTable.containsKey(d)) {
                FibonacciHeapNode<T> y = degreeTable.get(d);
                if (x.key > y.key) {
                    FibonacciHeapNode<T> temp = x;
                    x = y;
                    y = temp;
                }
                link(y, x);
                degreeTable.remove(d);
                d++;
            }
            degreeTable.put(d, x);
            current = current.right;
        } while (current != start);
        
        minNode = null;
        for (FibonacciHeapNode<T> node : degreeTable.values()) {
            if (minNode == null || node.key < minNode.key) {
                minNode = node;
            }
        }
    }

    private void link(FibonacciHeapNode<T> y, FibonacciHeapNode<T> x) {
        removeNode(y);
        y.parent = x;
        if (x.child == null) {
            x.child = y;
            y.right = y;
            y.left = y;
        } else {
            mergeLists(x.child, y);
        }
        x.degree++;
        y.mark = false;
    }
}
