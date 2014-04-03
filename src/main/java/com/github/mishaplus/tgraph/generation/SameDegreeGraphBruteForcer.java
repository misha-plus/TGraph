package com.github.mishaplus.tgraph.generation;

import com.github.mishaplus.tgraph.util.DirectedPseudographCreator;
import com.github.mishaplus.tgraph.util.MyEdge;
import com.github.mishaplus.tgraph.util.Util;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import org.jgrapht.graph.DirectedPseudograph;

import java.util.List;
import java.util.function.Consumer;

public class SameDegreeGraphBruteForcer {
    private final int vertexCount;
    private final int degree;
    private final Consumer<DirectedPseudograph<Integer, MyEdge>> handler;
    private final List<Integer> vertices;
    private final List<List<Integer>> decartPower;

    public SameDegreeGraphBruteForcer(
            int vertexCount,
            int degree,
            Consumer<DirectedPseudograph<Integer, MyEdge>> handler
    ) {
        this.vertexCount = vertexCount;
        this.degree = degree;
        this.handler = handler;
        vertices = Util.generateList(1, vertexCount);
        decartPower = Util.decartPower(vertices, degree);
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

        for (List<Integer> vTargets : decartPower) {
            vTargets.forEach(target -> edges.put(v, target));
            bruteGraph(v + 1, edges);
            edges.removeAll(v);
        }
    }
}
