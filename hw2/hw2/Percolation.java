package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private int size;
    private int openSites;
    private boolean[] openGrid;
    private WeightedQuickUnionUF grid;
    private WeightedQuickUnionUF fullGrid;

    public Percolation(int N) {
        size = N;
        openGrid = new boolean[N * N];
        grid = new WeightedQuickUnionUF(N * N + 2);
        fullGrid = new WeightedQuickUnionUF(N * N + 1);
    }

    private int xyTo1D(int r, int c) {
        return size * r + c;
    }

    public void open(int row, int col) {
        isValid(row, col);
        int intPos = xyTo1D(row, col);
        if (openGrid[intPos]) return;
        openGrid[intPos] = true;
        openSites++;

        for (int value : new int[]{-1, 1}) {
            int newRow = row + value;
            int newCol = col + value;
            if (newRow < size && newRow > -1) if (isOpen(newRow, col)) {
                grid.union(intPos, xyTo1D(newRow, col));
                fullGrid.union(intPos, xyTo1D(newRow, col));
            }
            if (newCol < size && newCol > -1) if (isOpen(row, newCol)) {
                grid.union(intPos, xyTo1D(row, newCol));
                fullGrid.union(intPos, xyTo1D(row, newCol));
            }
        }
        if (row == 0) {
            grid.union(size * size, intPos);
            fullGrid.union(size * size, intPos);
        }
        if (row == size - 1) grid.union(size * size + 1, intPos);
    }

    public boolean isOpen(int row, int col) {
        isValid(row, col);
        return openGrid[xyTo1D(row, col)];
    }

    public boolean isFull(int row, int col) {
        return fullGrid.connected(size * size, xyTo1D(row, col));
    }

    public int numberOfOpenSites() {
        return openSites;
    }

    public boolean percolates() {
        return grid.connected(size * size, size * size + 1);
    }

    private void isValid(int row, int col) {
        for (int value : new int[]{row, col}) {
            if (value > size - 1) throw new IndexOutOfBoundsException("element out of bounds");
            else if (value < 0) throw new IllegalArgumentException("illegal element");
        }
    }
}
