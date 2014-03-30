package com.github.mishaplus.tgraph.util;

import com.github.mishaplus.tgraph.IntegerMatrix;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import org.jgrapht.graph.DirectedPseudograph;
import org.junit.Assert;
import org.junit.Test;

import java.util.Set;

public class DirectedPseudographCreatorTest {
    @Test
    public void testCreateFromEdges() throws Exception {
        Multimap<Integer, Integer> edges = ImmutableMultimap.of(
                1, 2,
                1, 2,
                1, 1,
                2, 3,
                3, 4
        );

        Set<Integer> expectedVertices = Sets.newHashSet(1, 2, 3, 4);

        DirectedPseudograph<Integer, MyEdge> actualGraph = DirectedPseudographCreator.create(edges);

        Assert.assertEquals(expectedVertices, actualGraph.vertexSet());
        Assert.assertEquals(edges, getEdges(actualGraph));
    }

    private Multimap<Integer, Integer> getEdges(DirectedPseudograph<Integer, MyEdge> g) {
        Multimap<Integer, Integer> result = ArrayListMultimap.create();
        for (MyEdge actualEdge : g.edgeSet())
            result.put(g.getEdgeSource(actualEdge), g.getEdgeTarget(actualEdge));
        return result;
    }

    @Test
    public void testCreateFromMatrix() throws Exception {
        int[][] adj = new int[][]{
                { 1, 2, 3 },
                { 1, 0, 1 },
                { 2, 1, 3 }
        };

        Multimap<Integer, Integer> expectedEdges = ArrayListMultimap.create();
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                for (int k = 0; k < adj[i][j]; k++)
                    expectedEdges.put(i+1, j+1);

        Assert.assertEquals(
                expectedEdges,
                getEdges(DirectedPseudographCreator.create(new IntegerMatrix(adj)))
        );
    }
}
