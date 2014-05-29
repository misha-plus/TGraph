package com.github.mishaplus.tgraph.interestinggraphs;

import static org.junit.Assert.*;

import com.github.mishaplus.tgraph.GraphMarks;
import com.github.mishaplus.tgraph.MarkType;
import com.github.mishaplus.tgraph.numbersets.strategies.TernaryLogic;
import org.junit.Test;

public class TotSyncPartitionableInterestingCheckerTest {
    InterestingChecker checker = new TotSyncPartitionableInterestingChecker();
    GraphMarks marks = new GraphMarks();

    @Test
    public void testIsInteresting() throws Exception {
        marks.setMark(MarkType.isPartitionable,         TernaryLogic.No);
        marks.setMark(MarkType.isTotallySynchronizable, false);
        assertFalse(checker.isInteresting(null, marks));

        marks.setMark(MarkType.isPartitionable,         TernaryLogic.No);
        marks.setMark(MarkType.isTotallySynchronizable, true);
        assertFalse(checker.isInteresting(null, marks));

        marks.setMark(MarkType.isPartitionable,         TernaryLogic.Yes);
        marks.setMark(MarkType.isTotallySynchronizable, false);
        assertFalse(checker.isInteresting(null, marks));

        marks.setMark(MarkType.isPartitionable,         TernaryLogic.Yes);
        marks.setMark(MarkType.isTotallySynchronizable, true);
        assertTrue(checker.isInteresting(null, marks));
    }

    @Test
    public void testFilenamePrefix() throws Exception {
        assertEquals("totSyncPartitionable", checker.filenamePrefix());
    }

    @Test
    public void testGetDescription() throws Exception {
        assertEquals("partitionable & totallySynchronizable", checker.getDescription());
    }
}
