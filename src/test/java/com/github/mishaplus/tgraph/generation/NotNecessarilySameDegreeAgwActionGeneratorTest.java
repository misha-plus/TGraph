package com.github.mishaplus.tgraph.generation;

import com.github.mishaplus.tgraph.util.DirectedPseudographCreator;
import com.github.mishaplus.tgraph.util.MyEdge;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import org.jgrapht.graph.DirectedPseudograph;
import org.junit.Assert;
import org.junit.Test;

import java.util.Set;

public class NotNecessarilySameDegreeAgwActionGeneratorTest {
    @Test
    public void testBrute() throws Exception {
        Set<DirectedPseudograph<Integer, MyEdge>> expected = ImmutableSet.of(
                DirectedPseudographCreator.create(ImmutableMultimap.of(
                        1, 2, /**/ 2, 1, /**/ 1, 1
                )),
                DirectedPseudographCreator.create(ImmutableMultimap.of(
                        1, 2, /**/ 2, 1, /**/ 2, 2
                )),

                DirectedPseudographCreator.create(ImmutableMultimap.of(
                        1, 2, /**/ 2, 1, /**/ 1, 1, /**/ 2, 2
                )),

                DirectedPseudographCreator.create(ImmutableMultimap.of(
                        1, 2, /**/ 2, 1, /**/ 1, 1, /**/ 2, 1
                )),

                DirectedPseudographCreator.create(ImmutableMultimap.of(
                        1, 2, /**/ 2, 1, /**/ 2, 2, /**/ 1, 2
                ))
        );

        Set<DirectedPseudograph<Integer, MyEdge>> actual = Sets.newHashSet();
        new NotNecessarilySameDegreeAgwActionGenerator(2, 2, actual::add).bruteAll();

        Assert.assertEquals(Sets.newHashSet(expected), actual);
    }
}
