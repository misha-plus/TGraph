package com.github.mishaplus.tgraph;

import com.github.mishaplus.tgraph.util.MyEdge;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.jgrapht.graph.DirectedPseudograph;

import java.util.Map;
import java.util.Set;

public class Converter {
    public static IntegerMatrix toIntAdjArray(DirectedPseudograph<Integer, MyEdge> g) {
        Set<Integer> vertices = Sets.newTreeSet(g.vertexSet());
        Map<Integer, Integer> vertexToIndex = Maps.newTreeMap();
        {
            int i = 0;
            for (Integer vertex : vertices) {
                vertexToIndex.put(vertex, i);
                i++;
            }
        }

        int[][] result = new int[vertices.size()][vertices.size()];
        for (int i = 0; i < vertices.size(); i++)
            for (int j = 0; j < vertices.size(); j++)
                result[i][j] = 0;

        for (Integer from : vertices)
            for (MyEdge edge : g.outgoingEdgesOf(from)) {
                Integer to = g.getEdgeTarget(edge);
                result[vertexToIndex.get(from)][vertexToIndex.get(to)]++;
            }

        return new IntegerMatrix(result);
    }
}
