package com.github.mishaplus.tgraph.interestinggraphs;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.github.mishaplus.tgraph.GraphMarks;
import com.github.mishaplus.tgraph.util.DirectedPseudographCreator;
import com.github.mishaplus.tgraph.util.MyEdge;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import org.jgrapht.graph.DirectedPseudograph;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Set;

public class IntersectionInterestingCheckerTest {
    InterestingChecker first;
    InterestingChecker second;
    InterestingChecker third;

    InterestingChecker intersection;

    @Before
    public void setUp() {
        first  = mock(InterestingChecker.class);
        second = mock(InterestingChecker.class);
        third  = mock(InterestingChecker.class);

        intersection = new IntersectionInterestingChecker(first, second, third);
    }

    @Test
    public void testIsInteresting() throws Exception {
        Set<Boolean> booleanValues = ImmutableSet.of(true, false);
        for (boolean a : booleanValues)
            for (boolean b : booleanValues)
                for (boolean c : booleanValues) {
                    ImmutableMap.of(first, a, second, b, third, c)
                            .forEach((checker, value) -> when(checker.isInteresting(
                                    Mockito.<DirectedPseudograph<Integer, MyEdge>>any(),
                                    Mockito.<GraphMarks>any())
                            ).thenReturn(value));

                    if (a && b && c)
                        assertTrue(intersection.isInteresting(
                                DirectedPseudographCreator.create(),
                                new GraphMarks()
                        ));
                    else
                        assertFalse(intersection.isInteresting(
                                DirectedPseudographCreator.create(),
                                new GraphMarks()
                        ));
                }
    }

    @Test
    public void testFilenamePrefix() throws Exception {
        when(first.filenamePrefix()).thenReturn("first");
        when(second.filenamePrefix()).thenReturn("second");
        when(third.filenamePrefix()).thenReturn("third");

        assertEquals("(first)and(second)and(third)", intersection.filenamePrefix());
    }

    @Test
    public void testGetDescriptionWithoutBrackets() throws Exception {
        when(first.getDescription()).thenReturn("first");
        when(second.getDescription()).thenReturn("second");
        when(third.getDescription()).thenReturn("third");

        assertEquals("first & second & third", intersection.getDescription());
    }

    @Test
    public void testGetDescriptionWithBrackets() throws Exception {
        when(first.getDescription()).thenReturn("first");
        when(second.getDescription()).thenReturn("second%");
        when(third.getDescription()).thenReturn("third");

        assertEquals("first & (second%) & third", intersection.getDescription());
    }
}
