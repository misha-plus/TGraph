package com.github.mishaplus.tgraph.numbersets.strategies;

import com.google.common.collect.ImmutableMultiset;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

public class PrimeSumPartitioningStrategyTest {
    PartitioningStrategy primeStrategy = new PrimeSumPartitioningStrategy();

    @Test
    public void testIsCouldNotBePartedToSameSumSets() throws Exception {
        Assert.assertEquals(
                TernaryLogic.Unknown,
                primeStrategy.isCanBePartedToSameSumSets(ImmutableMultiset.of(
                        1, 1, 1, 1, 1
                ))
        );
        Assert.assertEquals(
                TernaryLogic.No,
                primeStrategy.isCanBePartedToSameSumSets(ImmutableMultiset.of(
                        2, 1, 1, 1
                ))
        );
        Assert.assertEquals(
                TernaryLogic.No,
                primeStrategy.isCanBePartedToSameSumSets(ImmutableMultiset.of(
                        5, 2
                ))
        );
    }

    @Test
    public void testGetDescription() throws Exception {
        Assert.assertThat(
                primeStrategy.getDescription(),
                Matchers.anyOf(Matchers.containsString("prime"), Matchers.containsString("Prime"))
        );
    }
}
