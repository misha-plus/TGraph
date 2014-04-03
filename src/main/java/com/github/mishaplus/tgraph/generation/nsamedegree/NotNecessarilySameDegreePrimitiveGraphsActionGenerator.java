package com.github.mishaplus.tgraph.generation.nsamedegree;

import com.github.mishaplus.tgraph.generation.PrimitiveGraphInspector;
import com.github.mishaplus.tgraph.generation.nsamedegree.NotNecessarilySameDegreeGraphBruteForcer;
import com.github.mishaplus.tgraph.util.MyEdge;
import org.jgrapht.graph.DirectedPseudograph;

import java.util.function.Consumer;

public class NotNecessarilySameDegreePrimitiveGraphsActionGenerator {
    private final int vertexCount;
    private final int outDegreeUpperBound;
    private final Consumer<DirectedPseudograph<Integer, MyEdge>> handler;

    public NotNecessarilySameDegreePrimitiveGraphsActionGenerator(
            int vertexCount,
            int outDegreeUpperBound,
            Consumer<DirectedPseudograph<Integer, MyEdge>> handler
    ) {
        this.vertexCount = vertexCount;
        this.outDegreeUpperBound = outDegreeUpperBound;
        this.handler = handler;
    }

    public void bruteAll() {
        new NotNecessarilySameDegreeGraphBruteForcer(vertexCount, outDegreeUpperBound, g -> {
            if (PrimitiveGraphInspector.isPrimitive(g))
                handler.accept(g);
        }).brute();
    }
}
