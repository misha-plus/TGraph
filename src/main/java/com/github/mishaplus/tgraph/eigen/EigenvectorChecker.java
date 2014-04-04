package com.github.mishaplus.tgraph.eigen;

import com.github.mishaplus.tgraph.IntegerMatrix;

/**
 * Created by michael on 04.04.14
 */
public class EigenvectorChecker {
    public static boolean isLeftEigenvector(
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
