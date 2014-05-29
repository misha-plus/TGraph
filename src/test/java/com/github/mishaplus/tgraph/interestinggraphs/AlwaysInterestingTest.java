package com.github.mishaplus.tgraph.interestinggraphs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class AlwaysInterestingTest {
    InterestingChecker checker = new AlwaysInteresting();

    @Test
    public void testIsInteresting() throws Exception {
        assertTrue(checker.isInteresting(null, null));
    }

    @Test
    public void testFilenamePrefix() throws Exception {
        assertEquals("graphs", checker.filenamePrefix());
    }

    @Test
    public void testGetDescription() throws Exception {
        assertEquals("All graphs", checker.getDescription());
    }
}
