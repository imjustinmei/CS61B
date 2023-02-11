package hw2;

import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Collections;
import java.util.ArrayList;
import java.lang.Math;

public class PercolationStats {
    private double mean;
    private double stdev;
    private double confidenceLow;
    private double confidenceHigh;

    public PercolationStats(int N, int T, PercolationFactory pf) {
        isValid(N, T);

        int times = 0;
        double[] trials = new double[T];
        int[] order = new int[N * N];
        for (int i = 0; i < N * N; i++) order[i] = i;

        while (times < T) {
            Percolation trial = pf.make(N);
            StdRandom.shuffle(order);
            int count = 0;

            while (!trial.percolates()) {
                int value = order[count++];
                trial.open(value / N, value % N);
            }

            double threshold = (double) count / N / N;
            trials[times++] = threshold;
        }

        mean = StdStats.mean(trials);
        stdev = StdStats.stddev(trials);

        confidenceLow = mean - 1.96 * stdev / Math.pow(T, .5);
        confidenceHigh = mean + 1.96 * stdev / Math.pow(T, .5);
    }

    private void isValid(int N, int T) {
        for (int value : new int[]{N, T}) if (value < 1) throw new IllegalArgumentException("illegal element");
    }

    public double mean() {
        return mean;
    }

    public double stddev() {
        return stdev;
    }

    public double confidenceLow() {
        return confidenceLow;
    }

    public double confidenceHigh() {
        return confidenceHigh;
    }
}
