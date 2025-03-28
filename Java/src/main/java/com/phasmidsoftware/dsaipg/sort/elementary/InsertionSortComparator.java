/*
  (c) Copyright 2018, 2019 Phasmid Software
 */
package com.phasmidsoftware.dsaipg.sort.elementary;

import com.phasmidsoftware.dsaipg.sort.generic.Sort;
import com.phasmidsoftware.dsaipg.sort.generic.SortWithHelper;
import com.phasmidsoftware.dsaipg.sort.helper.Helper;
import com.phasmidsoftware.dsaipg.util.config.Config;
import com.phasmidsoftware.dsaipg.util.config.Config_Benchmark;

import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

import static com.phasmidsoftware.dsaipg.sort.helper.InstrumentedComparatorHelper.getRunsConfig;

/**
 * A class for performing insertion sort using a comparator, extending functionality from SortWithHelper.
 * This includes methods for initialization and invocation of insertion sort,
 * along with specific utilities like counting inversions.
 *
 * @param <X> the type of elements to be sorted, which can be compared using a provided comparator.
 */
public class InsertionSortComparator<X> extends SortWithHelper<X> {
    /**
     * Constructor for InsertionSortComparator, which initializes the comparator with the provided helper.
     *
     * @param helper the Helper object to be used for managing the sorting process.
     */
    public InsertionSortComparator(Helper<X> helper) {
        super(helper);
    }

    /**
     * Constructor for any subclasses to use.
     *
     * @param description the description.
     * @param comparator  the comparator to use.
     * @param N           the number of elements expected.
     * @param nRuns       the number of runs to be expected (this is only significant when instrumenting).
     * @param config      the configuration.
     */
    protected InsertionSortComparator(String description, Comparator<X> comparator, int N, int nRuns, Config config) {
        super(description, comparator, N, nRuns, config);
    }

    /**
     * Constructor for InsertionSort
     *
     * @param N      the number elements we expect to sort.
     * @param nRuns  the number of runs to be expected (this is only significant when instrumenting).
     * @param config the configuration.
     */
    public InsertionSortComparator(Comparator<X> comparator, int N, int nRuns, Config config) {
        this(DESCRIPTION, comparator, N, nRuns, config);
    }

    /**
     * Sort the sub-array xs:from:to using insertion sort.
     *
     * @param xs   sort the array xs from "from" to "to".
     * @param from the index of the first element to sort
     * @param to   the index of the first element not to sort
     */
    public void sort(X[] xs, int from, int to) {
        final Helper<X> helper = getHelper();
        for (int i = from + 1; i < to; i++) {
            int j = i;
            while (j > from && helper.swapStableConditional(xs, j)) { 
                j--;
            }
        }
    }

    public static final String DESCRIPTION = "Insertion sort";

    /**
     * Sorts the given array in-place using the provided insertion sort comparator.
     *
     * @param <T> the generic type parameter that extends Comparable.
     * @param ts  the array of elements to be sorted, where elements must implement {@code Comparable}.
     *            The method modifies this array directly to produce the sorted order.
     * @throws RuntimeException if an IOException occurs during the sorting process.
     */
    public static <T extends Comparable<T>> void sort(T[] ts) {
        try (InsertionSortComparator<T> sort = new InsertionSortComparator<>(DESCRIPTION, Comparable::compareTo, ts.length, 1, Config.load(InsertionSortComparator.class))) {
            sort.mutatingSort(ts);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Creates a case-insensitive string sorter using an insertion sort comparator.
     *
     * @param n      the expected number of elements to be sorted.
     * @param config the configuration object containing necessary settings.
     * @return a {@code SortWithHelper<String>} instance configured for case-insensitive string sorting.
     */
    public static Sort<String> stringSorterCaseInsensitive(int n, Config config) {
        return new InsertionSortComparator<>(DESCRIPTION, String.CASE_INSENSITIVE_ORDER, n, getRunsConfig(config), config);
    }

    /**
     * This method is designed to count inversions in quadratic time, using insertion sort.
     *
     * @param ts  an array of comparable T elements.
     * @param <T> the underlying type of the elements.
     * @return the number of inversions in ts, which remains unchanged.
     */
    public static <T> long countInversions(T[] ts, Comparator<T> comparator) {
        final Config config = Config_Benchmark.setupConfigFixes();
        try (InsertionSortComparator<T> sorter = new InsertionSortComparator<>(comparator, ts.length, getRunsConfig(config), config)) {
            Helper<T> helper = sorter.getHelper();
            sorter.sort(ts, true);
            return helper.getFixes();
        }
    }

    
    public static void main(String[] args) {
        int[] sizes = {1000, 2000, 4000, 8000, 16000, 32000}; // Doubling method
        benchmarkRandomOrder(sizes);
        benchmarkOrdered(sizes);
        benchmarkPartiallyOrdered(sizes);
        benchmarkReverseOrdered(sizes);
    }

    private static void benchmarkRandomOrder(int[] sizes) {
        System.out.println("\nRandom");
        for (int n : sizes) {
            Integer[] array = new Integer[n];
            Random random = new Random();
            for (int i = 0; i < n; i++) array[i] = random.nextInt(n);
            double time = benchmarkSort(array);
            System.out.printf("%f\n",time);
        }
    }

    private static void benchmarkOrdered(int[] sizes) {
        System.out.println("\nOrdered");
        for (int n : sizes) {
            Integer[] array = new Integer[n];
            for (int i = 0; i < n; i++) array[i] = i;
            double time = benchmarkSort(array);
            System.out.printf("%f\n",time);
        }
    }

    private static void benchmarkPartiallyOrdered(int[] sizes) {
        System.out.println("\nPartially Ordered");

        for (int n : sizes) {
            Integer[] array = new Integer[n];
            Random random = new Random();
            for (int i = 0; i < n; i++) array[i] = (i % 5 == 0) ? random.nextInt(n) : i;
            double time = benchmarkSort(array);
            System.out.printf("%f\n",time);
        }
    }

    private static void benchmarkReverseOrdered(int[] sizes) {
        System.out.println("\nReverse Ordered");
        for (int n : sizes) {
            Integer[] array = new Integer[n];
            for (int i = 0; i < n; i++) array[i] = n-i;
            double time = benchmarkSort(array);
            System.out.printf("%f\n",time);
        }
    }

    private static double benchmarkSort(Integer[] array) {
        Integer[] copy = Arrays.copyOf(array, array.length);
        long start = System.nanoTime();
        for (int i = 1; i < copy.length; i++) {
            int key = copy[i];
            int j = i-1;
            while (j >= 0 && copy[j] > key) {
            	copy[j+1] = copy[j];
                j--;
            }
            copy[j+1] = key;
        }
        long end = System.nanoTime();
        return (end-start) / 1000000.0;
    }
}