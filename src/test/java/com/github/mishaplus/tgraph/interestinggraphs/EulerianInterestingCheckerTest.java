package com.github.mishaplus.tgraph.interestinggraphs;

import static org.junit.Assert.*;

import com.github.mishaplus.tgraph.GraphMarks;
import com.github.mishaplus.tgraph.MarkType;
import org.junit.Test;

public class EulerianInterestingCheckerTest {
    InterestingChecker checker = new EulerianInterestingChecker();

    @Test
    public void testIsInteresting() throws Exception {
        GraphMarks marks = new GraphMarks();
        marks.setMark(MarkType.isEulerian, true);
        assertTrue(checker.isInteresting(null, marks));
    }

    @Test
    public void testIsNotInteresting() throws Exception {
        GraphMarks marks = new GraphMarks();
        marks.setMark(MarkType.isEulerian, false);
        assertFalse(checker.isInteresting(null, marks));
    }

    @Test
    public void testFilenamePrefix() throws Exception {
        assertEquals("eulerian", checker.filenamePrefix());
    }

    @Test
    public void testGetDescription() throws Exception {
        assertEquals("eulerian", checker.getDescription());
    }
}
