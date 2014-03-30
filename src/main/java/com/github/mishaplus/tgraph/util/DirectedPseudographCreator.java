package com.github.mishaplus.tgraph.util;

import com.github.mishaplus.tgraph.IntegerMatrix;
import com.google.common.base.Preconditions;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.jgrapht.graph.DirectedPseudograph;

public class DirectedPseudographCreator {
    public static DirectedPseudograph<Integer, MyEdge> create() {
        return new DirectedPseudograph<>(MyEdge.class);
    }

    public static DirectedPseudograph<Integer, MyEdge> create(
            Multimap<Integer, Integer> edges
    ) {
        DirectedPseudograph<Integer, MyEdge> result = DirectedPseudographCreator.create();
        //noinspection RedundantTypeArguments
        edges.entries().forEach(edge ->
                Util.<Integer, MyEdge>addEdge(result, edge.getKey(), edge.getValue())
        );
        return result;
    }

    public static DirectedPseudograph<Integer, MyEdge> create(
            IntegerMatrix adjacencyMatrix
    ) {
        Preconditions.checkArgument(adjacencyMatrix.n == adjacencyMatrix.m);
        Multimap<Integer, Integer> edges = ArrayListMultimap.create();
        for (int i = 0; i < adjacencyMatrix.n; i++)
            for (int j = 0; j < adjacencyMatrix.m; j++)
                for (int k = 0; k < adjacencyMatrix.matrix[i][j]; k++)
                    edges.put(i+1, j+1);
        return create(edges);
    }
}
