package com.github.mishaplus.tgraph.util;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import org.jgrapht.DirectedGraph;
import org.jgrapht.alg.StrongConnectivityInspector;

import java.math.BigInteger;
import java.util.Map;
import java.util.function.Consumer;

public class AperiodicInspector<V, E> {
    private final DirectedGraph<V, E> graph;

    public AperiodicInspector(DirectedGraph<V, E> graph) {
        Preconditions.checkArgument(
                new StrongConnectivityInspector<>(graph).isStronglyConnected(),
                "Graph '%s' must be strongly connected", graph
        );
        this.graph = graph;
    }

    public boolean isAperiodic() {
        Map<V, Integer> coloring = Maps.newHashMap();
        graph.vertexSet().forEach(v -> {
            if (!coloring.containsKey(v))
                dfs(v, 0, graph, coloring);
        });

        final BigInteger[] gcd = {BigInteger.ZERO};
        Consumer<Integer> appendToGcd
                = num -> { gcd[0] = gcd[0].gcd(BigInteger.valueOf(num)); };
        for (E edge : graph.edgeSet()) {
            int sourceColor = coloring.get(graph.getEdgeSource(edge));
            int targetColor = coloring.get(graph.getEdgeTarget(edge));
            appendToGcd.accept(targetColor - sourceColor - 1);
        }
        return gcd[0].equals(BigInteger.ONE);
    }

    private void dfs(V v, int currentColor, DirectedGraph<V, E> g, Map<V, Integer> colors) {
        if (colors.keySet().contains(v))
            return;
        colors.put(v, currentColor);
        for (E edge : g.outgoingEdgesOf(v)) {
            V target = g.getEdgeTarget(edge);
            dfs(target, currentColor+1, g, colors);
        }
    }
}
