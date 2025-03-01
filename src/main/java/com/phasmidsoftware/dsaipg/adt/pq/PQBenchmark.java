package com.phasmidsoftware.dsaipg.adt.pq;

import java.util.*;
import com.phasmidsoftware.dsaipg.adt.pq.*;
import com.phasmidsoftware.dsaipg.util.Benchmark_Timer;


public class PQBenchmark {
	private static final int INSERT_COUNT = 16000;
    private static final int DELETE_COUNT = 4000;
    private static final int MAX_CAPACITY = 4095;

    public static void main(String[] args) {
        System.out.println("Testing PriorityQueue with and without Floyd's Trick...");
        runTest(false);
        runTest(true);
    }

    private static void runTest(boolean useFloyd) {
        Random random = new Random();
        PriorityQueue<Integer> pq = new PriorityQueue<Integer>(MAX_CAPACITY, true, Comparator.naturalOrder(), useFloyd);
        long startTime, endTime;
        System.out.println("\nTesting with Floyd's Trick: " + useFloyd);
        startTime = System.nanoTime();
        for (int i = 0; i < INSERT_COUNT; i++) {
            pq.give(random.nextInt());
        }
        endTime = System.nanoTime();
        System.out.println("Insertion time: " + (endTime - startTime) / 1_000_000.0 + " ms");
        startTime = System.nanoTime();
        for (int i = 0; i < DELETE_COUNT; i++) {
            try {
				pq.take();
			} catch (PQException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        endTime = System.nanoTime();
        System.out.println("Deletion time: " + (endTime - startTime) / 1_000_000.0 + " ms");
    }
}
