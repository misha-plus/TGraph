package com.github.mishaplus.tgraph.eigen;

import com.github.mishaplus.tgraph.Converter;
import com.github.mishaplus.tgraph.IntegerMatrix;
import com.github.mishaplus.tgraph.util.MyEdge;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.primitives.Ints;
import org.apache.commons.math3.util.ArithmeticUtils;
import org.jblas.ComplexDouble;
import org.jblas.ComplexDoubleMatrix;
import org.jblas.DoubleMatrix;
import org.jblas.Eigen;
import org.jgrapht.graph.DirectedPseudograph;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

public class SameOutDegreeGraphEigenvector {
    public List<Integer> getFriedmanEigenvectorWithRelativelyPrimeComponents(
            DirectedPseudograph<Integer, MyEdge> g
    ) throws EigenvectorNotFoundException {
        Set<Integer> vertices = g.vertexSet();
        Preconditions.checkArgument(vertices.size() > 0);
        int degree = g.outDegreeOf(vertices.iterator().next());
        for (Integer vertex: vertices)
            Preconditions.checkArgument(g.outDegreeOf(vertex) == degree);

        IntegerMatrix intMatrix = Converter.toIntAdjArray(g);
        List<Integer> eigenvector = getEigenvectorByBruteForce(intMatrix, degree);
        if (eigenvector == null)
            throw new EigenvectorNotFoundException();

        return toRelativePrime(eigenvector);
    }

    private List<Integer> toRelativePrime(List<Integer> list) {
        int[] gcd = {0};
        list.forEach(elem -> {
            gcd[0] = ArithmeticUtils.gcd(gcd[0], elem);
        });

        return Lists.newArrayList(list.stream().map(elem -> elem/gcd[0]).iterator());
    }

    private List<Integer> getEigenvectorByBruteForce(IntegerMatrix intMatrix, int degree) {
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

    private List<Integer> getPossibleFriedmanEigenvector(IntegerMatrix intMatrix, int degree) {
        DoubleMatrix doubleMatrix = intMatrix.toDoubleMatrix();

        ComplexDouble[] friedmanEigenvector = getSomeFriedmanEigenvector(doubleMatrix, degree);

        checkRealNonNegative(friedmanEigenvector);

        int[] possibleIntFriedmanEigenvector = toInteger(friedmanEigenvector);

        Preconditions.checkArgument(
                isLeftEigenvector(intMatrix, degree, possibleIntFriedmanEigenvector)
        );

        return Ints.asList(possibleIntFriedmanEigenvector);
    }

    private ComplexDouble[] getSomeFriedmanEigenvector(DoubleMatrix doubleMatrix, int degree) {
        ComplexDouble[] leftEigenvalues = Eigen.eigenvalues(doubleMatrix.transpose()).toArray();
        ComplexDoubleMatrix[] leftEigenvectors = Eigen.eigenvectors(doubleMatrix.transpose());

        int friedmanIndex = -1;
        for (int i = 0; i < leftEigenvalues.length; i++) {
            ComplexDouble eigenvalue = leftEigenvalues[i];
            if (eigenvalue.isReal() && eq(eigenvalue.real(), degree)) {
                friedmanIndex = i;
                break;
            }
        }

        Preconditions.checkArgument(friedmanIndex != -1, "Friedman eigenvalue not found");

        return leftEigenvectors[friedmanIndex].toArray();
    }

    private void checkRealNonNegative(ComplexDouble[] friedmanEigenvector) {
        for (ComplexDouble friedmanEigenvectorComponent : friedmanEigenvector) {
            Preconditions.checkArgument(
                    friedmanEigenvectorComponent.isReal(),
                    "Friedman eigenvector component is complex"
            );
            Preconditions.checkArgument(
                    friedmanEigenvectorComponent.real() >= -0.05,
                    "Friedman eigenvector component is negative"
            );
        }
    }

    private int[] toInteger(ComplexDouble[] friedmanEigenvector) {
        return Arrays.stream(friedmanEigenvector)
                .mapToInt(comp -> toInt(comp.real()))
                .toArray();
    }

    private boolean eq(double x, int y) {
        return toInt(x) == y;
    }

    private int toInt(double x) {
        return (int) (x + 0.1);
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
