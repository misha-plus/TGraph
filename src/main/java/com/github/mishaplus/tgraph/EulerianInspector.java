package com.github.mishaplus.tgraph;

import org.jgrapht.graph.DirectedPseudograph;

/**
 * Created by michael on 06.04.14
 */
public class EulerianInspector<V, E> {
    public boolean isGraphEulerianCycle(DirectedPseudograph<V, E> g) {
        for (V v : g.vertexSet())
            if (g.inDegreeOf(v) != g.outDegreeOf(v))
                return false;
        return true;
    }
}
