package com.github.mishaplus.tgraph.eigen;

import com.github.mishaplus.tgraph.IntegerMatrix;
import com.github.mishaplus.tgraph.util.DirectedPseudographCreator;
import com.github.mishaplus.tgraph.util.MyEdge;
import org.jgrapht.graph.DirectedPseudograph;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class SameOutDegreeGraphEigenvectorTest {
    @Test
    public void testGetLeftEigenvectorWithRelativelyPrimeComponents() throws Exception {
        DirectedPseudograph<Integer, MyEdge> input = DirectedPseudographCreator.create(
            new IntegerMatrix(new int[][] {
                    { 1, 2, 0 },
                    { 0, 1, 2 },
                    { 1, 1, 1 }
            })
        );

        List<Integer> friedmanEigenvector = new SameOutDegreeGraphEigenvector()
                .getFriedmanEigenvectorWithRelativelyPrimeComponents(input);

        List<Integer> expected = Arrays.asList(1, 2, 2);

        Assert.assertEquals(expected, friedmanEigenvector);
    }
}
