package com.github.mishaplus.tgraph.generation;

import com.github.mishaplus.tgraph.util.DirectedPseudographCreator;
import com.github.mishaplus.tgraph.util.MyEdge;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import org.hamcrest.Matchers;
import org.jgrapht.graph.DirectedPseudograph;
import org.junit.Assert;
import org.junit.Test;

import java.util.Set;

public class GenerateSameDegreePrimitivePseudographsTest {
    @Test
    public void testGenerate2x2NonIsomorphic() throws Exception {
        Set<DirectedPseudograph<Integer, MyEdge>> expectedClass1 = ImmutableSet.of(
                DirectedPseudographCreator.create(ImmutableMultimap.of(
                        1, 2, /**/ 2, 1, /**/ 1, 1, /**/ 2, 2
                ))
        );

        Set<DirectedPseudograph<Integer, MyEdge>> expectedClass2 = ImmutableSet.of(
                DirectedPseudographCreator.create(ImmutableMultimap.of(
                        1, 2, /**/ 2, 1, /**/ 1, 1, /**/ 2, 1
                )),
                DirectedPseudographCreator.create(ImmutableMultimap.of(
                        1, 2, /**/ 2, 1, /**/ 2, 2, /**/ 1, 2
                ))
        );

        Set<DirectedPseudograph<Integer, MyEdge>> expectedUnion = Sets.union(
                expectedClass1, expectedClass2
        );

        Set<DirectedPseudograph<Integer, MyEdge>> generated
                = new GenerateSameDegreePrimitivePseudographs(2, 2).generateAllNonIsomorphic();

        for (DirectedPseudograph<Integer, MyEdge> generatedGraph: generated)
            Assert.assertThat(generatedGraph, Matchers.isIn(expectedUnion));

        checkRepresenterContains(expectedClass1, generated);
        checkRepresenterContains(expectedClass2, generated);
    }

    private <E> void checkRepresenterContains(
            Set<E> representers,
            Set<E> whereToCheck
    ) throws Exception {
        boolean isContainsRepresenter = false;
        for (E representer: representers)
            if (whereToCheck.contains(representer))
                isContainsRepresenter = true;
        Assert.assertTrue(isContainsRepresenter);
    }

    @Test
    public void testGenerate3x2NonIsomorphic() throws Exception {
        Set<DirectedPseudograph<Integer, MyEdge>> generated
                = new GenerateSameDegreePrimitivePseudographs(3, 2).generateAllNonIsomorphic();
        Assert.assertEquals(12, generated.size());
    }

    @Test
    public void testGenerate1x5NonIsomorphic() throws Exception {
        Set<DirectedPseudograph<Integer, MyEdge>> expected = ImmutableSet.of(
                DirectedPseudographCreator.create(ImmutableMultimap.<Integer, Integer>builder()
                        .put(1, 1)
                        .put(1, 1)
                        .put(1, 1)
                        .put(1, 1)
                        .put(1, 1)
                        .build()
                )
        );

        Set<DirectedPseudograph<Integer, MyEdge>> generated
                = new GenerateSameDegreePrimitivePseudographs(1, 5).generateAllNonIsomorphic();
        Assert.assertEquals(expected, generated);
    }
}
