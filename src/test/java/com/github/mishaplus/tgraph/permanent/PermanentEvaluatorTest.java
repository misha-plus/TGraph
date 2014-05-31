package com.github.mishaplus.tgraph.permanent;

import static org.junit.Assert.*;

import com.github.mishaplus.tgraph.IntegerMatrix;
import org.junit.Test;

public class PermanentEvaluatorTest {
    @Test
    public void testEvaluate() throws Exception {
        IntegerMatrix adj = new IntegerMatrix(new int[][] {
                { 0, 1, 1 },
                { 1, 1, 0 },
                { 1, 1, 0 }
        });
        int expectedPermanent = 2;
        int actualPermanent = new PermanentEvaluator().evaluate(adj);
        assertEquals(expectedPermanent, actualPermanent);
    }
}
