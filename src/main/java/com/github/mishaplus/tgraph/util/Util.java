package com.github.mishaplus.tgraph.util;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.jgrapht.Graph;
import org.jgrapht.graph.DirectedPseudograph;

import java.util.Arrays;
import java.util.List;

public class Util {
    public static List<Integer> generateList(int lowerBound, int upperBound) {
        List<Integer> result = Lists.newArrayList();
        for (int i = lowerBound; i <= upperBound; i++)
            result.add(i);
        return result;
    }

    public static <V> List<List<V>> decartMultiply(List<List<V>> left, List<V> right) {
        List<List<V>> result = Lists.newArrayList();
        left.forEach(leftTuple -> right.forEach(rightComponent -> {
            List<V> addend = Lists.newArrayList(leftTuple);
            addend.add(rightComponent);
            result.add(addend);
        }));
        return result;
    }

    public static <V> List<List<V>> decartPower(List<V> base, int power) {
        Preconditions.checkArgument(power > 0);
        List<List<V>> accum = Lists.newArrayList();
        final List<List<V>> accumFinal = accum;
        base.forEach(elem -> accumFinal.add(Arrays.asList(elem)));
        for (int i = 2; i <= power; i++) {
            accum = decartMultiply(accum, base);
        }
        return accum;
    }

    public static <V, E> void addVerticesIfNotContains(Graph<V, E> g, List<V> vs) {
        //noinspection Convert2streamapi
        for (V v : vs)
            if (!g.containsVertex(v))
                g.addVertex(v);
    }

    public static void addEdge(Graph<Integer, MyEdge> g, int sourceVertex, int targetVertex) {
        addVerticesIfNotContains(g, Arrays.asList(sourceVertex, targetVertex));
        int edgePower = g.getAllEdges(sourceVertex, targetVertex).size();
        g.addEdge(
                sourceVertex,
                targetVertex,
                new MyEdge(sourceVertex, targetVertex, edgePower + 1)
        );
    }

    public static <V, E> boolean isGraphHaveSameDegree(DirectedPseudograph<V, E> g) {
        int someVertexDegree = getSomeDegree(g);
        return isGraphHaveDegree(g, someVertexDegree);
    }

    public static <V, E> boolean isGraphHaveDegree(DirectedPseudograph<V, E> g, int degree) {
        for (V vertex: g.vertexSet())
            if (g.outDegreeOf(vertex) != degree)
                return false;
        return true;
    }

    public static <V, E> int getSomeDegree(DirectedPseudograph<V, E> g) {
        return g.outDegreeOf(g.vertexSet().iterator().next());
    }
}
