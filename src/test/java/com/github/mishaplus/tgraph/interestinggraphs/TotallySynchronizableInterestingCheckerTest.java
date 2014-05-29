package com.github.mishaplus.tgraph.interestinggraphs;

import static org.junit.Assert.*;

import com.github.mishaplus.tgraph.GraphMarks;
import com.github.mishaplus.tgraph.MarkType;
import org.junit.Test;

public class TotallySynchronizableInterestingCheckerTest {
    InterestingChecker checker = new TotallySynchronizableInterestingChecker();

    @Test
    public void testIsInteresting() throws Exception {
        GraphMarks marks = new GraphMarks();

        marks.setMark(MarkType.isTotallySynchronizable, true);
        assertTrue(checker.isInteresting(null, marks));

        marks.setMark(MarkType.isTotallySynchronizable, false);
        assertFalse(checker.isInteresting(null, marks));
    }

    @Test
    public void testFilenamePrefix() throws Exception {
        assertEquals("totallySynchronizable", checker.filenamePrefix());
    }

    @Test
    public void testGetDescription() throws Exception {
        assertEquals("totallySynchronizable", checker.getDescription());
    }
}
