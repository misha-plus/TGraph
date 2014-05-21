package com.github.mishaplus.tgraph.diophantineequation;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

import com.github.mishaplus.tgraph.IntegerMatrix;
import org.junit.Test;

public class FixedDegreeEulerianFinderTest {

    @Test
    public void testCreateFlowGraph() throws Exception {
        IntegerMatrix edges = new IntegerMatrix(new int[][] {
                new int[] {4, 3, 0},
                new int[] {2, 1, 0},
                new int[] {0, 0, 1}
        });

        String expectedStringRepresentation = "([6, 7, 0, 1, 2, 3, 4, 5], [" +
                "(0-[7]>3)=(0,3), (0-[7]>4)=(0,4), " +
                "(1-[7]>3)=(1,3), (1-[7]>4)=(1,4), " +
                "(2-[7]>5)=(2,5), (6-[0]>0)=(6,0), " +
                "(6-[4]>1)=(6,1), (6-[6]>2)=(6,2), " +
                "(3-[1]>7)=(3,7), (4-[3]>7)=(4,7), " +
                "(5-[6]>7)=(5,7)" +
                "])";
        assertEquals(
                expectedStringRepresentation,
                new FixedDegreeEulerianFinder(edges, 7).createFlowGraph().toString()
        );
    }

    @Test
    public void testIsSolveable() throws Exception {
        IntegerMatrix edges = new IntegerMatrix(new int[][] {
                new int[] {4, 3, 0},
                new int[] {2, 1, 0},
                new int[] {0, 0, 1}
        });

        assertTrue(new FixedDegreeEulerianFinder(edges, 7).isSolveable());
        assertTrue(new FixedDegreeEulerianFinder(edges, 8).isSolveable());
        assertTrue(new FixedDegreeEulerianFinder(edges, 9).isSolveable());
        assertTrue(new FixedDegreeEulerianFinder(edges, 100).isSolveable());
    }

    @Test
    public void testIsNotSolveableNeedsIncreaseDegree() throws Exception {
        IntegerMatrix edges = new IntegerMatrix(new int[][] {
                new int[] {4, 3, 0},
                new int[] {2, 1, 0},
                new int[] {0, 0, 1}
        });

        assertFalse(new FixedDegreeEulerianFinder(edges, 6).isSolveable());
    }

    @Test
    public void testIsNotSolveableWithAllDegrees() throws Exception {
        IntegerMatrix edges = new IntegerMatrix(new int[][] {
                new int[] {0, 1, 1},
                new int[] {1, 1, 0},
                new int[] {1, 1, 0}
        });

        assertFalse(new FixedDegreeEulerianFinder(edges, 10).isSolveable());
        assertFalse(new FixedDegreeEulerianFinder(edges, 1).isSolveable());
    }
}
