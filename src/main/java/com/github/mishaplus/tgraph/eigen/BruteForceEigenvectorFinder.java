package com.github.mishaplus.tgraph.eigen;

import com.github.mishaplus.tgraph.IntegerMatrix;
import com.google.common.primitives.Ints;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

/**
 * Created by michael on 04.04.14
 */
public class BruteForceEigenvectorFinder implements EigenvectorFinder {
    @Override
    public List<Integer> findPositiveEigenvector(IntegerMatrix intMatrix, int degree) {
        int[] possibleEigenvector = new int[intMatrix.n];
        Arrays.fill(possibleEigenvector, 0);
        int valueFrom = 1;
        int valueTo   = 10;

        boolean isFriedmanEigenvectorFound = brute(
                possibleEigenvector,
                valueFrom,
                valueTo,
                0,
                v -> isLeftEigenvector(intMatrix, degree, possibleEigenvector)
        );

        if (isFriedmanEigenvectorFound)
            return Ints.asList(possibleEigenvector);
        else
            return null;
    }

    private boolean brute(
            int[] array,
            int valueFrom,
            int valueTo,
            int depth,
            Predicate<int[]> isSatisfy
    ) {
        if (depth == array.length)
            return isSatisfy.test(array);

        for (int value = valueFrom; value <= valueTo; value++) {
            array[depth] = value;
            if (brute(array, valueFrom, valueTo, depth+1, isSatisfy))
                return true;
        }

        return false;
    }

    private boolean isLeftEigenvector(
            IntegerMatrix matrix,
            int eigenvalue,
            int[] possibleLeftEigenvector
    ) {
        IntegerMatrix possibleLeftEigenvectorMatrix
                = new IntegerMatrix(new int[][] { possibleLeftEigenvector });

        IntegerMatrix left = possibleLeftEigenvectorMatrix.multiply(matrix);
        IntegerMatrix right = possibleLeftEigenvectorMatrix.multiply(eigenvalue);
        return left.equals(right);
    }
}
