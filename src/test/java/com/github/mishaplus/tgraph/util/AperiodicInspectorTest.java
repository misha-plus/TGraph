package com.github.mishaplus.tgraph.util;

import org.jgrapht.graph.DirectedPseudograph;
import org.junit.Assert;
import org.junit.Test;

public class AperiodicInspectorTest {
    @Test
    public void testIsAperiodic() throws Exception {
        DirectedPseudograph<Integer, MyEdge> g = DirectedPseudographCreator.create();
        Util.addEdge(g, 1, 2);
        Util.addEdge(g, 2, 4);
        Util.addEdge(g, 4, 1);
        Util.addEdge(g, 4, 5);
        Util.addEdge(g, 5, 4);

        Assert.assertTrue(new AperiodicInspector<>(g).isAperiodic());
    }

    @Test
    public void testIsNotAperiodic() throws Exception {
        DirectedPseudograph<Integer, MyEdge> g = DirectedPseudographCreator.create();

        Util.addEdge(g, 1, 2);
        Util.addEdge(g, 2, 4);
        Util.addEdge(g, 4, 1);

        Util.addEdge(g, 4, 6);
        Util.addEdge(g, 6, 7);
        Util.addEdge(g, 7, 4);

        Assert.assertFalse(new AperiodicInspector<>(g).isAperiodic());
    }
}
