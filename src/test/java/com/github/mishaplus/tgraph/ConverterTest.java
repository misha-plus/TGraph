package com.github.mishaplus.tgraph;

import com.github.mishaplus.tgraph.util.DirectedPseudographCreator;
import com.github.mishaplus.tgraph.util.MyEdge;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.jgrapht.graph.DirectedPseudograph;
import org.junit.Assert;
import org.junit.Test;

public class ConverterTest {
    @Test
    public void testToIntAdjArray() throws Exception {
        int[][] expected = new int[][] {
                new int[] { 2, 1, 0, 0 },
                new int[] { 0, 0, 0, 0 },
                new int[] { 3, 2, 0, 4 },
                new int[] { 0, 0, 0, 0 },
        };

        Multimap<Integer, Integer> edges = ArrayListMultimap.create();
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++)
                for (int k = 0; k < expected[i][j]; k++)
                    edges.put(i + 10, j + 10);

        DirectedPseudograph<Integer, MyEdge> g = DirectedPseudographCreator.create(edges);
        IntegerMatrix actual = Converter.toIntAdjArray(g);
        Assert.assertArrayEquals(expected, actual.matrix);
    }
}
