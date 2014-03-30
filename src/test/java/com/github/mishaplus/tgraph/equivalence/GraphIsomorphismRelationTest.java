package com.github.mishaplus.tgraph.equivalence;

import com.github.mishaplus.tgraph.util.DirectedPseudographCreator;
import com.github.mishaplus.tgraph.util.MyEdge;
import com.google.common.collect.ImmutableMultimap;
import org.jgrapht.graph.DirectedPseudograph;
import org.junit.Assert;
import org.junit.Test;

public class GraphIsomorphismRelationTest {
    @Test
    public void testIsEquivalent() throws Exception {
        DirectedPseudograph<Integer, MyEdge> a = DirectedPseudographCreator.create(
                ImmutableMultimap.of(
                        1, 2,
                        2, 3,
                        3, 1,
                        3, 1,
                        2, 2
                )
        );

        DirectedPseudograph<Integer, MyEdge> b = DirectedPseudographCreator.create(
                ImmutableMultimap.of(
                        5, 6,
                        5, 6,
                        6, 7,
                        7, 5,
                        7, 7
                )
        );

        Assert.assertTrue(new GraphIsomorphismRelation().isEquivalent(a, b));
    }

    @Test
    public void testIsNotEquivalent1() throws Exception {
        DirectedPseudograph<Integer, MyEdge> a = DirectedPseudographCreator.create(
                ImmutableMultimap.of(
                        1, 2,
                        2, 3,
                        3, 1,
                        3, 1,
                        2, 2
                )
        );

        DirectedPseudograph<Integer, MyEdge> b = DirectedPseudographCreator.create(
                ImmutableMultimap.of(
                        1, 2,
                        1, 2,
                        2, 3,
                        3, 1
                )
        );

        Assert.assertFalse(new GraphIsomorphismRelation().isEquivalent(a, b));
    }

    @Test
    public void testIsNotEquivalent2() throws Exception {
        DirectedPseudograph<Integer, MyEdge> a = DirectedPseudographCreator.create(
                ImmutableMultimap.of(
                        1, 2,
                        2, 3,
                        3, 1,
                        3, 1,
                        2, 2
                )
        );

        DirectedPseudograph<Integer, MyEdge> b = DirectedPseudographCreator.create(
                ImmutableMultimap.of(
                        1, 2,
                        2, 3,
                        2, 3,
                        3, 1,
                        3, 3
                )
        );
        Assert.assertFalse(new GraphIsomorphismRelation().isEquivalent(a, b));
    }
}
