package com.github.mishaplus.tgraph.generation;

import com.github.mishaplus.tgraph.util.AperiodicInspector;
import org.jgrapht.alg.StrongConnectivityInspector;
import org.jgrapht.graph.DirectedPseudograph;

/**
 * Created by michael on 03.04.14
 */
public class PrimitiveGraphInspector {
    public static <V, E> boolean isPrimitive(DirectedPseudograph<V, E> g) {
        boolean isStronglyConnected = new StrongConnectivityInspector<>(g).isStronglyConnected();
        if (isStronglyConnected) {
            boolean isAperiodic = new AperiodicInspector<>(g).isAperiodic();
            if (isAperiodic)
                return true;
        }
        return false;
    }
}
