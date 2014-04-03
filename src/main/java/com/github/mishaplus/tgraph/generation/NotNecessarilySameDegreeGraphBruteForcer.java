package com.github.mishaplus.tgraph.generation;

import com.github.mishaplus.tgraph.util.DirectedPseudographCreator;
import com.github.mishaplus.tgraph.util.MyEdge;
import com.github.mishaplus.tgraph.util.Util;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Maps;
import org.jgrapht.graph.DirectedPseudograph;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class NotNecessarilySameDegreeGraphBruteForcer {
    private final int vertexCount;
    private final int outDegreeUpperBound;
    private final Consumer<DirectedPseudograph<Integer, MyEdge>> handler;
    private final List<Integer> vertices;
    private final Map<Integer, List<List<Integer>>> decartPowers;

    public NotNecessarilySameDegreeGraphBruteForcer(
            int vertexCount,
            int outDegreeUpperBound,
            Consumer<DirectedPseudograph<Integer, MyEdge>> handler
    ) {
        this.vertexCount = vertexCount;
        this.outDegreeUpperBound = outDegreeUpperBound;
        this.handler = handler;
        vertices = Util.generateList(1, vertexCount);
        decartPowers = Maps.newHashMap();
        for (int degree = 1; degree <= outDegreeUpperBound; degree++) {
            decartPowers.put(degree, Util.decartPower(vertices, degree));
        }
    }

    public void brute() {
        bruteGraph(1, ArrayListMultimap.<Integer, Integer>create());
    }

    private void bruteGraph(
            int v,
            ListMultimap<Integer, Integer> edges
    ) {
        if (v > vertexCount) {
            handler.accept(DirectedPseudographCreator.create(edges));
            return;
        }

        for (int degree = 1; degree <= outDegreeUpperBound; degree++) {
            for (List<Integer> vTargets : decartPowers.get(degree)) {
                vTargets.forEach(target -> edges.put(v, target));
                bruteGraph(v + 1, edges);
                edges.removeAll(v);
            }
        }
    }
}
