package com.github.mishaplus.tgraph.interestinggraphs;

import static org.junit.Assert.*;

import com.github.mishaplus.tgraph.GraphMarks;
import com.github.mishaplus.tgraph.MarkType;
import com.github.mishaplus.tgraph.numbersets.strategies.TernaryLogic;
import org.junit.Test;

public class NotEulerianAndPartitionableInterestingCheckerTest {
    InterestingChecker checker = new NotEulerianAndPartitionableInterestingChecker();
    GraphMarks marks = new GraphMarks();

    @Test
    public void testIsInteresting() throws Exception {
        marks.setMark(MarkType.isPartitionable, TernaryLogic.No);
        marks.setMark(MarkType.isEulerian,      false);
        assertFalse(checker.isInteresting(null, marks));

        marks.setMark(MarkType.isPartitionable, TernaryLogic.No);
        marks.setMark(MarkType.isEulerian,      true);
        assertFalse(checker.isInteresting(null, marks));

        marks.setMark(MarkType.isPartitionable, TernaryLogic.Yes);
        marks.setMark(MarkType.isEulerian,      false);
        assertTrue(checker.isInteresting(null, marks));

        marks.setMark(MarkType.isPartitionable, TernaryLogic.Yes);
        marks.setMark(MarkType.isEulerian,      true);
        assertFalse(checker.isInteresting(null, marks));
    }

    @Test
    public void testFilenamePrefix() throws Exception {
        assertEquals("notEulerianAndPartitionable", checker.filenamePrefix());
    }

    @Test
    public void testGetDescription() throws Exception {
        assertEquals("!eulerian && partitionable", checker.getDescription());
    }
}
