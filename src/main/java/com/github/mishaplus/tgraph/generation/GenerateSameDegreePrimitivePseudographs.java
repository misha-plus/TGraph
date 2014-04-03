package com.github.mishaplus.tgraph.generation;

import com.github.mishaplus.tgraph.util.MyEdge;
import com.google.common.collect.Sets;
import org.jgrapht.graph.DirectedPseudograph;

import java.util.Set;

public class GenerateSameDegreePrimitivePseudographs {
    private final int vertexCount;
    private final int vertexDegree;

    public GenerateSameDegreePrimitivePseudographs(int vertexCount, int vertexDegree) {
        this.vertexCount = vertexCount;
        this.vertexDegree = vertexDegree;
    }

    public Set<DirectedPseudograph<Integer, MyEdge>> generateAllNonIsomorphic() {
        Set<DirectedPseudograph<Integer, MyEdge>> generatedNotExceedDegree
                = new NotNecessarilySameDegreePrimitiveGraphsSetsGenerator(
                        vertexCount, vertexDegree
                ).generateAllNonIsomophic();

        return Sets.newHashSet(generatedNotExceedDegree
                .stream()
                .filter(this::isGraphHaveSameDegree)
                .iterator()
        );
    }

    private <V, E> boolean isGraphHaveSameDegree(DirectedPseudograph<V, E> g) {
        for (V vertex: g.vertexSet())
            if (g.outDegreeOf(vertex) != vertexDegree)
                return false;
        return true;
    }
}