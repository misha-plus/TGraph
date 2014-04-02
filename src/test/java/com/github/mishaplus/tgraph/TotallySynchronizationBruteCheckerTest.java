package com.github.mishaplus.tgraph;

import com.github.mishaplus.tgraph.util.DirectedPseudographCreator;
import com.github.mishaplus.tgraph.util.MyEdge;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import org.jgrapht.graph.DirectedPseudograph;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by michael on 02.04.14
 */
public class TotallySynchronizationBruteCheckerTest {
    @Test
    public void testCheckTrue() throws Exception {
        Multimap<Integer, Integer> edges = ImmutableMultimap.<Integer, Integer>builder()
                .put(1, 2).put(1, 2)
                .put(2, 2).put(2, 3)
                .put(3, 1).put(3, 1)
                .build();
        DirectedPseudograph<Integer, MyEdge> g = DirectedPseudographCreator.create(edges);
        Assert.assertTrue(TotallySynchronizationBruteChecker.checkTS(g));
    }

    @Test
    public void testCheckFalse() throws Exception {
        Multimap<Integer, Integer> edges = ImmutableMultimap.<Integer, Integer>builder()
                .put(1, 2).put(1, 3)
                .put(2, 1).put(2, 2)
                .put(3, 1).put(3, 2)
                .build();
        DirectedPseudograph<Integer, MyEdge> g = DirectedPseudographCreator.create(edges);
        Assert.assertFalse(TotallySynchronizationBruteChecker.checkTS(g));
    }
}
