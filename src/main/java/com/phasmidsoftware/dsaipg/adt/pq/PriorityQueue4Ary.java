package com.phasmidsoftware.dsaipg.adt.pq;

import java.util.Comparator;
import java.util.function.BiPredicate;
import java.util.function.Consumer;

public class PriorityQueue4Ary<K> {
    
    private final boolean max;
    private final int first;
    private final Comparator<K> comparator;
    private K[] binHeap;
    private int last;
    private final boolean floyd;
    private final int d = 4;

    public PriorityQueue4Ary(int n, boolean max, Comparator<K> comparator, boolean floyd) {
        this.max = max;
        this.first = 1;
        this.comparator = comparator;
        this.last = 0;
        this.floyd = floyd;
        this.binHeap = (K[]) new Object[n + first];
    }

    public boolean isEmpty() {
        return last == 0;
    }

    public int size() {
        return last;
    }

    public void give(K key) {
        if (last == binHeap.length - first)
            resize(binHeap.length * 2);
        binHeap[++last + first - 1] = key;
        swimUp(last + first - 1);
    }

    public K take() {
        if (isEmpty()) throw new RuntimeException("Priority queue is empty");
        if (floyd) return doTake(this::snake);
        else return doTake(this::sink);
    }

    private K doTake(Consumer<Integer> f) {
        K result = binHeap[first];
        swap(first, last-- + first - 1);
        f.accept(first);
        binHeap[last + first] = null;
        return result;
    }

    private void sink(int k) {
        doHeapify(k, (a, b) -> !unordered(a, b));
    }

    private void snake(int k) {
        swimUp(doHeapify(k, (a, b) -> !unordered(a, b)));
    }

    private void swimUp(int k) {
        int i = k;
        while (i > first && unordered(parent(i), i)) {
            swap(i, parent(i));
            i = parent(i);
        }
    }

    private int doHeapify(int k, BiPredicate<Integer, Integer> p) {
        int i = k;
        while (firstChild(i) <= last + first - 1) {
            int bestChild = firstChild(i);
            for (int j = 1; j < d; j++) {
                int candidate = firstChild(i) + j;
                if (candidate <= last + first - 1 && unordered(bestChild, candidate)) {
                    bestChild = candidate;
                }
            }
            if (p.test(i, bestChild)) break;
            swap(i, bestChild);
            i = bestChild;
        }
        return i;
    }

    private boolean unordered(int i, int j) {
        return (comparator.compare(binHeap[i], binHeap[j]) > 0) ^ max;
    }

    private void swap(int i, int j) {
        K tmp = binHeap[i];
        binHeap[i] = binHeap[j];
        binHeap[j] = tmp;
    }

    private int parent(int k) {
        return (k - first - 1) / d + first;
    }

    private int firstChild(int k) {
        return d * (k - first) + first + 1;
    }

    private void resize(int newSize) {
        K[] newBinHeap = (K[]) new Object[newSize];
        System.arraycopy(binHeap, 0, newBinHeap, 0, binHeap.length);
        binHeap = newBinHeap;
    }

}