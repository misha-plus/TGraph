package com.github.mishaplus.tgraph.util;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class UtilTest {
    @Test
    public void testGenerateList() throws Exception {
        Assert.assertEquals(Arrays.asList(3, 4, 5, 6, 7), Util.generateList(3, 7));
    }

    @Test
    public void testDecartMultiply() throws Exception {
        List<List<Integer>> accum = Arrays.asList(
                Arrays.asList(1, 2, 3),
                Arrays.asList(1, 3, 5, 6),
                Arrays.asList(4, 7)
        );

        List<Integer> multiplicand = Arrays.asList(4, 9);

        List<List<Integer>> expected = Arrays.asList(
                Arrays.asList(1, 2, 3, 4),
                Arrays.asList(1, 2, 3, 9),
                Arrays.asList(1, 3, 5, 6, 4),
                Arrays.asList(1, 3, 5, 6, 9),
                Arrays.asList(4, 7, 4),
                Arrays.asList(4, 7, 9)
        );

        Assert.assertEquals(expected, Util.decartMultiply(accum, multiplicand));
    }

    @Test
    public void testDecartPower() throws Exception {
        List<Integer> base = Arrays.asList(1, 3);
        int power = 3;
        List<List<Integer>> expected = Arrays.asList(
                Arrays.asList(1, 1, 1),
                Arrays.asList(1, 1, 3),
                Arrays.asList(1, 3, 1),
                Arrays.asList(1, 3, 3),
                Arrays.asList(3, 1, 1),
                Arrays.asList(3, 1, 3),
                Arrays.asList(3, 3, 1),
                Arrays.asList(3, 3, 3)
        );

        Assert.assertEquals(expected, Util.decartPower(base, power));
    }
}
