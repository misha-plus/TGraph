package com.github.mishaplus.tgraph;

import com.google.common.collect.ImmutableMap;
import org.jblas.DoubleMatrix;
import org.junit.Assert;
import org.junit.Test;

import java.util.function.Function;

public class IntegerMatrixTest {
    @Test
    public void testSwapRowsAndLines() throws Exception {
        int[][] input = new int[][] {
                new int[] { 1, 2, 3 },
                new int[] { 4, 5, 6 },
                new int[] { 7, 8, 9 }
        };

        Function<Integer, Integer> permutation = idx -> ImmutableMap.of(
                1, 2,
                3, 1,
                2, 3
        ).get(idx + 1) - 1;

        int[][] expected = new int[][] {
                new int[] { 5, 6, 4 },
                new int[] { 8, 9, 7 },
                new int[] { 2, 3, 1 }
        };

        Assert.assertEquals(
                new IntegerMatrix(expected),
                new IntegerMatrix(input).swapRowsAndLines(permutation)
        );
    }

    @Test
    public void testToDoubleMatrix() throws Exception {
        int[][] input = new int[][] {
                new int[] { 5, 6, 4 },
                new int[] { 8, 9, 7 }
        };

        double[][] expected = new double[][] {
                { 5., 6., 4. },
                { 8., 9., 7. }
        };

        Assert.assertEquals(
                new DoubleMatrix(expected),
                new IntegerMatrix(input).toDoubleMatrix()
        );
    }

    @Test
    public void testMultiplyMatrix() throws Exception {
        int[][] left = new int[][] {
                new int[] {1, 2, 3},
                new int[] {4, 5, 6}
        };

        int[][] right = new int[][] {
                {2, 3, 4, 5},
                {5, 6, 7, 8},
                {6, 7, 8, 9}
        };

        int[][] expected = new int[][] {
                {30, 36, 42, 48},
                {69, 84, 99, 114}
        };

        IntegerMatrix lMatix = new IntegerMatrix(left);
        IntegerMatrix rMatrix = new IntegerMatrix(right);
        IntegerMatrix expMatrix = new IntegerMatrix(expected);

        Assert.assertEquals(expMatrix, lMatix.multiply(rMatrix));
    }

    @Test
    public void testMultiplyCoeff() throws Exception {
        int[][] input = new int[][] {
                new int[] { 1, 2, 3 },
                new int[] { 3, 4, 5 }
        };

        int coeff = 3;

        int[][] expected = new int[][] {
                new int[] { 3, 6,  9  },
                new int[] { 9, 12, 15 }
        };

        IntegerMatrix actual = new IntegerMatrix(input).multiply(coeff);

        Assert.assertEquals(
                new IntegerMatrix(expected),
                actual
        );

        Assert.assertArrayEquals(
                expected,
                actual.matrix
        );
    }
}
