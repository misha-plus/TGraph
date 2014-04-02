package com.github.mishaplus.tgraph.generation;

import com.github.mishaplus.tgraph.util.AperiodicInspector;
import com.github.mishaplus.tgraph.util.DirectedPseudographCreator;
import com.github.mishaplus.tgraph.util.MyEdge;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import org.jgrapht.alg.StrongConnectivityInspector;
import org.jgrapht.graph.DirectedPseudograph;
import org.junit.Assert;
import org.junit.Test;

import java.util.Set;

public class NotNecessarilySameDegreePrimitiveGraphsSetsGeneratorTest {
    @Test
    public void testGenerateAll() throws Exception {
        Set<DirectedPseudograph<Integer, MyEdge>> expected = Sets.newHashSet();
        new NotNecessarilySameDegreePrimitiveGraphsActionGenerator(2, 5, g -> {
            if (new StrongConnectivityInspector<>(g).isStronglyConnected()
                    && new AperiodicInspector<>(g).isAperiodic())
                expected.add(g);
        }).bruteAll();
        Assert.assertEquals(
                expected,
                new NotNecessarilySameDegreePrimitiveGraphsSetsGenerator(2, 5).generateAll()
        );
    }

    @Test
    public void testGenerateAllNonIsomophic() throws Exception {
        Set<DirectedPseudograph<Integer, MyEdge>> expectedClass1 = ImmutableSet.of(
                DirectedPseudographCreator.create(ImmutableMultimap.of(
                        1, 2, /**/ 2, 1, /**/ 1, 1
                )),
                DirectedPseudographCreator.create(ImmutableMultimap.of(
                        1, 2, /**/ 2, 1, /**/ 2, 2
                ))
        );

        Set<DirectedPseudograph<Integer, MyEdge>> expectedClass2 = ImmutableSet.of(
                DirectedPseudographCreator.create(ImmutableMultimap.of(
                        1, 2, /**/ 2, 1, /**/ 1, 1, /**/ 2, 2
                ))
        );

        Set<DirectedPseudograph<Integer, MyEdge>> expectedClass3 = ImmutableSet.of(
                DirectedPseudographCreator.create(ImmutableMultimap.of(
                        1, 2, /**/ 2, 1, /**/ 1, 1, /**/ 2, 1
                )),
                DirectedPseudographCreator.create(ImmutableMultimap.of(
                        1, 2, /**/ 2, 1, /**/ 2, 2, /**/ 1, 2
                ))
        );

        Set<Set<DirectedPseudograph<Integer, MyEdge>>> expectedClasses = ImmutableSet.of(
                expectedClass1, expectedClass2, expectedClass3
        );

        Set<DirectedPseudograph<Integer, MyEdge>> actual
                = new NotNecessarilySameDegreePrimitiveGraphsSetsGenerator(2, 2).generateAllNonIsomophic();

        for (DirectedPseudograph<Integer, MyEdge> gottenPseudograph: actual) {
            boolean isInSomeClass = false;
            for (Set<DirectedPseudograph<Integer, MyEdge>> someClass: expectedClasses)
                if (someClass.contains(gottenPseudograph))
                    isInSomeClass = true;
            Assert.assertTrue(isInSomeClass);
        }

        for (Set<DirectedPseudograph<Integer, MyEdge>> expectedClass: expectedClasses) {
            boolean isClassPresented = false;
            for (DirectedPseudograph<Integer, MyEdge> representer : expectedClass) {
                if (actual.contains(representer))
                    isClassPresented = true;
            }
            Assert.assertTrue(isClassPresented);
        }
    }

    @Test
    public void generateAllNonIsomorphic3VertexAnd2Degree() throws Exception {
        Set<DirectedPseudograph<Integer, MyEdge>> actual
                = new NotNecessarilySameDegreePrimitiveGraphsSetsGenerator(3, 2).generateAllNonIsomophic();
        //for (Graph o : actual)
        //   show(o);
        Assert.assertEquals(23, actual.size());
    }
}
