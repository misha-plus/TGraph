package com.github.mishaplus.tgraph.generation;

import com.github.mishaplus.tgraph.util.AperiodicInspector;
import com.github.mishaplus.tgraph.util.MyEdge;
import org.jgrapht.alg.StrongConnectivityInspector;
import org.jgrapht.graph.DirectedPseudograph;

import java.util.function.Consumer;

public class NotNecessarilySameDegreeAgwActionGenerator {
    private final int vertexCount;
    private final int outDegreeUpperBound;
    private final Consumer<DirectedPseudograph<Integer, MyEdge>> handler;

    public NotNecessarilySameDegreeAgwActionGenerator(
            int vertexCount,
            int outDegreeUpperBound,
            Consumer<DirectedPseudograph<Integer, MyEdge>> handler
    ) {
        this.vertexCount = vertexCount;
        this.outDegreeUpperBound = outDegreeUpperBound;
        this.handler = handler;
    }

    public void bruteAll() {
        new GraphBruteForcer(vertexCount, outDegreeUpperBound, g -> {
            boolean isStronglyConnected = new StrongConnectivityInspector<>(g).isStronglyConnected();
            if (isStronglyConnected) {
                boolean isAperiodic = new AperiodicInspector<>(g).isAperiodic();
                if (isAperiodic)
                    handler.accept(g);
            }
        }).brute();
    }
}
