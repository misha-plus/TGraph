package com.github.mishaplus.tgraph;

import static org.junit.Assert.assertEquals;

import com.github.mishaplus.tgraph.util.DirectedPseudographCreator;
import com.github.mishaplus.tgraph.util.MyEdge;
import org.jgrapht.graph.DirectedPseudograph;
import org.junit.Test;

public class PermutationCounterTest {
    @Test
    public void testCount() throws Exception {
        DirectedPseudograph<Integer, MyEdge> graph = DirectedPseudographCreator.create(
                new IntegerMatrix(new int[][] {
                        { 0, 1, 1 },
                        { 2, 0, 0 },
                        { 0, 1, 1 }
                })
        );
        int expectedPermutationsCount = 2;
        int actualPermutationsCount = new PermutationCounter().count(graph);
        assertEquals(expectedPermutationsCount, actualPermutationsCount);
    }
}
