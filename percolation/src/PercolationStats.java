/**
 * Created by wuhao on 2017-06-01.
 */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    // perform trials independent experiments on an n-by-n grid

    //    int[] xOpenSites; // number of open sites when perculate
//    double[] xProb; // xOpenSites / total sites
    int gridSize, totalSites, mTrials;
    double mean, stddev, confidenceLo, confidenceHi;

    // n: size of the grid
    // trails: number of times of the percolation tests
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException("PercolationStatus n or trials smaller or equal to 0");
        }

        int[] xOpenSites = new int[trials];
        double[] xProb = new double[trials];
        StdRandom.setSeed(System.currentTimeMillis());

        gridSize = n;
        mTrials = trials;
        totalSites = n * n;
        int randRow, randCol;

        for (int i = 0; i < trials; ++i) {
            Percolation perc = new Percolation(n);

            while (!perc.percolates() &&
                    perc.numberOfOpenSites() != totalSites) {
                // generate an random index
                randRow = StdRandom.uniform(n) + 1;
                randCol = StdRandom.uniform(n) + 1;

                if (!perc.isOpen(randRow, randCol)) {
                    perc.open(randRow, randCol);
                }
            }

//            System.out.printf("Number of open sites: %d\n", perc.numberOfOpenSites());
            xOpenSites[i] = perc.numberOfOpenSites();
            xProb[i] = xOpenSites[i] / (double) totalSites;

            mean = StdStats.mean(xProb);
            stddev = StdStats.stddev(xProb);
            confidenceLo = mean - 1.96 * stddev / Math.sqrt(mTrials);
            confidenceHi = mean + 1.96 * stddev / Math.sqrt(mTrials);
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return mean;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return stddev;
    }

    // low  endpoint of 95% confidence interval
    public double confidenceLo() {
        return confidenceLo;
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return confidenceHi;
    }

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Require n and T in PercolationStats");
        }

        // args[0]: n, size of the grid
        int n = Integer.parseInt(args[0]);
        // args[1]: T, testing times
        int t = Integer.parseInt(args[1]);
        PercolationStats ps = new PercolationStats(n, t);

        System.out.printf("mean = %f\n", ps.mean());
        System.out.printf("stddev = %f\n", ps.stddev());
        System.out.printf("95%% confidence interval = [%f, %f]",
                ps.confidenceLo(), ps.confidenceHi());

    }
}
