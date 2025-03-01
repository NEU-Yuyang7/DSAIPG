package com.phasmidsoftware.dsaipg.adt.pq;

import java.util.*;
import java.util.PriorityQueue;

import com.phasmidsoftware.dsaipg.adt.pq.*;
import com.phasmidsoftware.dsaipg.util.Benchmark_Timer;

public class FibonacciHeapBenchmark {
	public static void main(String[] args) {
        final int M = 4095;
        final int insertions = 4000;
        final int deletions = 4000;
        FibonacciHeap<Integer> fibHeap = new FibonacciHeap<>();
        PriorityQueue<Integer> overflowQueue = new PriorityQueue<>();
        Random random = new Random();
        long startTime = System.nanoTime();
        
        for (int i = 0; i < insertions; i++) {
            int value = random.nextInt(1000000);
            FibonacciHeapNode<Integer> node = new FibonacciHeapNode<>(value, value);
            fibHeap.insert(node);
            if (fibHeap.size() > M) {
                overflowQueue.add(fibHeap.extractMin().data);
            }
        }
        for (int i = 0; i < deletions; i++) {
            if (fibHeap.size() > 0) {
                fibHeap.extractMin();
            }
        }

        FibonacciHeapNode<Integer> minNode = fibHeap.findMin();
        int highestPriority = (minNode != null) ? minNode.data : -1;
        
        long endTime = System.nanoTime();
        double elapsedTimeInMillis = (endTime - startTime) / 1_000_000.0;
        
        System.out.println("Highest priority element: " + highestPriority);
        System.out.println("Execution time: " + elapsedTimeInMillis + " ms");
    }
}
