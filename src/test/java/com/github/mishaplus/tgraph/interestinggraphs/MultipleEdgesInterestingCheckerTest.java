package com.github.mishaplus.tgraph.interestinggraphs;

import static org.junit.Assert.*;

import com.github.mishaplus.tgraph.util.DirectedPseudographCreator;
import com.github.mishaplus.tgraph.util.MyEdge;
import com.google.common.collect.ImmutableMultimap;
import org.jgrapht.graph.DirectedPseudograph;
import org.junit.Test;

public class MultipleEdgesInterestingCheckerTest {
    InterestingChecker checker = new MultipleEdgesInterestingChecker();

    @Test
    public void testIsInteresting() throws Exception {
        DirectedPseudograph<Integer, MyEdge> graph = DirectedPseudographCreator.create(
                ImmutableMultimap.of(
                        1, 2,
                        1, 2,
                        1, 3,
                        1, 4
                )
        );
        assertTrue(checker.isInteresting(graph, null));
    }

    @Test
    public void testIsNotInteresting() throws Exception {
        DirectedPseudograph<Integer, MyEdge> graph = DirectedPseudographCreator.create(
                ImmutableMultimap.of(
                        1, 2,
                        1, 3,
                        1, 4
                )
        );
        assertFalse(checker.isInteresting(graph, null));
    }

    @Test
    public void testFilenamePrefix() throws Exception {
        assertEquals("multipleEdgesGraphs", checker.filenamePrefix());
    }

    @Test
    public void testGetDescription() throws Exception {
        assertEquals("multipleEdges", checker.getDescription());
    }
}
