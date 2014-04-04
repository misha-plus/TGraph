package com.github.mishaplus.tgraph.eigen;

import com.github.mishaplus.tgraph.IntegerMatrix;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import solver.Fraction;
import solver.Matrix;
import solver.RationalField;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.SortedSet;

/**
 * Created by michael on 04.04.14
 */
public class GaussEigenvectorFinderImpl implements EigenvectorFinder {
    @Override
    public List<Integer> findPositiveEigenvector(IntegerMatrix matrix, int eigenvalue) {
        Preconditions.checkArgument(matrix.n == matrix.m);
        Matrix<Fraction> fractionMatrix = new Matrix<>(matrix.n, matrix.m+1, RationalField.FIELD);
        for (int i = 0; i < matrix.n; i++)
            for (int j = 0; j < matrix.n; j++)
                if (i != j) {
                    fractionMatrix.set(i, j, new Fraction(matrix.matrix[i][j], 1));
                } else {
                    fractionMatrix.set(i, j, new Fraction(matrix.matrix[i][j] - eigenvalue, 1));
                }

        for (int i = 0; i < matrix.n; i++)
            fractionMatrix.set(i, matrix.n, new Fraction(0, 1));

        transpose(fractionMatrix, matrix.n);

        fractionMatrix.reducedRowEchelonForm();

        SortedMap<Integer, Fraction> someFriedmanEigenvector = getSomeFriedmanEigenvector(
                fractionMatrix,
                matrix.n
        );

        List<Fraction> fractionEigenvector = Lists.newArrayList();
        for (int i = 0; i < matrix.n; i++)
            fractionEigenvector.add(someFriedmanEigenvector.get(i));

        BigInteger downCommon = BigInteger.ONE;
        for (Fraction component : fractionEigenvector)
            downCommon = downCommon.multiply(component.denominator);

        List<BigInteger> bigIntEigenvector = Lists.newArrayList();
        for (Fraction component : fractionEigenvector) {
            BigInteger downCommonDivDown = downCommon.divide(component.denominator);
            bigIntEigenvector.add(component.numerator.multiply(downCommonDivDown));
        }

        BigInteger gcd = gcd(bigIntEigenvector);

        List<Integer> result = Lists.newArrayList();
        for (BigInteger component : bigIntEigenvector)
            result.add((int) component.divide(gcd).longValue());

        return result;
    }

    private <T> void transpose(Matrix<T> matrix, int n) {
        for (int i = 0; i < n; i++)
            for (int j = i; j < n; j++) {
                T a = matrix.get(i, j);
                T b = matrix.get(j, i);

                matrix.set(i, j, b);
                matrix.set(j, i, a);
            }
    }

    private BigInteger gcd(List<BigInteger> bigIntEigenvector) {
        BigInteger gcd = BigInteger.ZERO;
        for (BigInteger component : bigIntEigenvector)
            gcd = gcd.gcd(component);
        return gcd;
    }

    private int findEigenvectorParameterIndex(Matrix<Fraction> fractionMatrix, int vectorLength) {
        Fraction zeroFraction = new Fraction(0, 1);
        for (int j = 0; j < vectorLength; j++) {
            int includeCount = 0;
            for (int i = 0; i < vectorLength; i++) {
                if (!fractionMatrix.get(i, j).equals(zeroFraction))
                    includeCount++;
            }
            if (includeCount == vectorLength - 1) {
                return j;
            }
        }
        return -1;
    }

    private SortedMap<Integer, Fraction> getSomeFriedmanEigenvector(
            Matrix<Fraction> fractionMatrix,
            int n
    ) {
        int parameterIndex = findEigenvectorParameterIndex(fractionMatrix, n);
        SortedMap<Integer, Fraction> result = Maps.newTreeMap();
        result.put(parameterIndex, new Fraction(1, 1));
        for (int i = 0; i < n; i++) {
            int nonZeroIndex = -1;
            for (int j = 0; j < n; j++)
                if (j != parameterIndex && !fractionMatrix.get(i, j).equals(new Fraction(0, 1))) {
                    nonZeroIndex = j;
                    break;
                }

            if (nonZeroIndex == -1)
                continue;

            // ax + by = 0
            // ax = -b*y
            // x = (-b/a)*y
            // x = -b/a, when y = 1

            Fraction a = fractionMatrix.get(i, nonZeroIndex);
            Fraction b = fractionMatrix.get(i, parameterIndex);

            Fraction negB = RationalField.FIELD.negate(b);
            Fraction x = RationalField.FIELD.divide(negB, a);

            result.put(nonZeroIndex, x);
        }
        Preconditions.checkArgument(result.size() == n);
        return result;
    }
}
