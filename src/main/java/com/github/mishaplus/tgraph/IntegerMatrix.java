package com.github.mishaplus.tgraph;

import com.google.common.base.Preconditions;
import org.jblas.DoubleMatrix;

import java.util.Arrays;
import java.util.function.Function;

public class IntegerMatrix {
    public final int n;
    public final int m;
    public final int[][] matrix;

    public IntegerMatrix(int[][] matrix) {
        n = matrix.length;
        m = matrix[0].length;
        Preconditions.checkArgument(n > 0);
        Preconditions.checkArgument(m > 0);
        for (int[] row : matrix)
            Preconditions.checkArgument(row.length == m, "Rows must have same length");

        this.matrix = new int[n][m];
        for (int i = 0; i < n; i++)
            this.matrix[i] = matrix[i].clone();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof IntegerMatrix))
            return false;
        IntegerMatrix other = (IntegerMatrix) obj;
        return Arrays.deepEquals(matrix, other.matrix);
    }

    public IntegerMatrix swapRowsAndLines(Function<Integer, Integer> permutation) {
        Preconditions.checkArgument(n == m);
        int[][] result = new int[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                result[i][j] = matrix[permutation.apply(i)][permutation.apply(j)];
            }
        }
        return new IntegerMatrix(result);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("IntegerMatrix("+ n + ", " + m + ")[\n");
        for (int i = 0; i < n; i++) {
            result.append("[");
            for (int j = 0; j < m; j++) {
                result.append(matrix[i][j]);
                if (j + 1 < m)
                    result.append(", ");
            }
            result.append("]\n");
        }
        result.append("]");
        return result.toString();
    }

    public DoubleMatrix toDoubleMatrix() {
        double[][] result = new double[n][m];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < m; j++)
                result[i][j] = matrix[i][j];
        return new DoubleMatrix(result);
    }

    public IntegerMatrix multiply(IntegerMatrix other) {
        Preconditions.checkArgument(m == other.n);
        int[][] result = new int[n][other.m];
        for (int[] row : result)
            Arrays.fill(row, 0);

        for (int i = 0; i < n; i++)
            for (int j = 0; j < other.m; j++)
                for (int s = 0; s < m; s++)
                    result[i][j] += matrix[i][s] * other.matrix[s][j];

        return new IntegerMatrix(result);
    }

    public IntegerMatrix multiply(int coefficient) {
        Function<int[], int[]> multiplyRow = row -> Arrays.stream(row)
                .map(cell -> coefficient * cell).toArray();
        int[][] result = new int[n][];
        for (int i = 0; i < n; i++)
            result[i] = multiplyRow.apply(matrix[i]);
        return new IntegerMatrix(result);
    }
}
