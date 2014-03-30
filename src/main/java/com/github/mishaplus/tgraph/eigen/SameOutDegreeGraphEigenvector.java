package com.github.mishaplus.tgraph.eigen;

import com.github.mishaplus.tgraph.Converter;
import com.github.mishaplus.tgraph.IntegerMatrix;
import com.github.mishaplus.tgraph.util.MyEdge;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.jblas.ComplexDouble;
import org.jblas.ComplexDoubleMatrix;
import org.jblas.DoubleMatrix;
import org.jblas.Eigen;
import org.jgrapht.graph.DirectedPseudograph;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class SameOutDegreeGraphEigenvector {
    public List<Integer> getLeftEigenvectorWithRelativelyPrimeComponents(
            DirectedPseudograph<Integer, MyEdge> g
    ) {
        Set<Integer> vertices = g.vertexSet();
        Preconditions.checkArgument(vertices.size() > 0);
        int degree = g.outDegreeOf(vertices.iterator().next());
        for (Integer vertex: vertices)
            Preconditions.checkArgument(g.outDegreeOf(vertex) == degree);

        IntegerMatrix intMatrix = Converter.toIntAdjArray(g);
        DoubleMatrix doubleMatrix = intMatrix.toDoubleMatrix();

        ComplexDouble[] friedmanEigenvector = getSomeFriedmanEigenvector(doubleMatrix, degree);

        checkRealNonNegative(friedmanEigenvector);

        int[] possibleIntFriedmanEigenvector = toInteger(friedmanEigenvector);

        Preconditions.checkArgument(
                isFriedmanEigenvector(intMatrix, degree, possibleIntFriedmanEigenvector)
        );

        return Lists.newArrayList(Arrays.stream(possibleIntFriedmanEigenvector)
                .mapToObj(Integer::new).iterator());
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

    private boolean isFriedmanEigenvector(
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
