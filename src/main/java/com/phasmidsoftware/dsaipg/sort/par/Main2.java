package com.phasmidsoftware.dsaipg.sort.par;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;

public class Main2 {
    public static void main(String[] args) {
        processArgs(args);
        System.out.println("Degree of parallelism: " + ForkJoinPool.getCommonPoolParallelism());
        System.out.println("Using cutoff: " + ParSort.cutoff);
        System.out.println("Using maxDepth: " + ParSort.maxDepth);

        Random random = new Random();
        int[] array = new int[2000000];

        int[] depths = {1, 2, 3, 4};

        for (int depth : depths) {
            ParSort.maxDepth = depth;
            System.out.println("Testing with depth: " + depth);
            ArrayList<Long> timeList = new ArrayList<>();

            for (int j = 50; j < 100; j++) {
                ParSort.cutoff = 10000 * (j + 1);
                long time;
                long startTime = System.currentTimeMillis();

                for (int t = 0; t < 10; t++) {
                    for (int i = 0; i < array.length; i++) array[i] = random.nextInt(10000000);
                    ParSort.sort(array, 0, array.length, 0);
                }

                long endTime = System.currentTimeMillis();
                time = (endTime - startTime);
                timeList.add(time);
                System.out.println("cutoff: " + ParSort.cutoff + "\t depth: " + depth + "\t 10 times Time: " + time + "ms");
            }
        }
    }

    private static void processArgs(String[] args) {
        for (int i = 0; i < args.length - 1; i++) {
            if (args[i].equalsIgnoreCase("-cutoff")) {
                ParSort.cutoff = Integer.parseInt(args[i + 1]);
            } else if (args[i].equalsIgnoreCase("-depth")) {
                ParSort.maxDepth = Integer.parseInt(args[i + 1]);
            }
        }
    }
}
