package com.github.mishaplus.tgraph.generation;

import com.github.mishaplus.tgraph.util.DirectedPseudographCreator;
import com.github.mishaplus.tgraph.util.MyEdge;
import com.github.mishaplus.tgraph.util.Util;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import org.jgrapht.graph.DirectedPseudograph;

import java.util.List;
import java.util.function.Consumer;

public class GraphBruteForcer {
    private final int vertexCount;
    private final int outDegreeUpperBound;
    private final Consumer<DirectedPseudograph<Integer, MyEdge>> handler;

    public GraphBruteForcer(
            int vertexCount,
            int outDegreeUpperBound,
            Consumer<DirectedPseudograph<Integer, MyEdge>> handler
    ) {
        this.vertexCount = vertexCount;
        this.outDegreeUpperBound = outDegreeUpperBound;
        this.handler = handler;
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

        List<Integer> vertices = Util.generateList(1, vertexCount);

        for (int degree = 1; degree <= outDegreeUpperBound; degree++) {
            List<List<Integer>> vTargetsPack = Util.decartPower(vertices, degree);
            for (List<Integer> vTargets : vTargetsPack) {
                vTargets.forEach(target -> edges.put(v, target));
                bruteGraph(v + 1, edges);
                edges.removeAll(v);
            }
        }
    }
}
