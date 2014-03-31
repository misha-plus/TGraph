package com.github.mishaplus.tgraph.numbersets.strategies;

import com.google.common.collect.ImmutableMultiset;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by michael on 31.03.14
 */
public class BruteForceStrategyTest {
    PartitioningStrategy partitioningStrategy = new BruteForceStrategy();

    @Test
    public void testIsCanBePartedToSameSumSets() throws Exception {
        Assert.assertEquals(
                TernaryLogic.Yes,
                partitioningStrategy.isCanBePartedToSameSumSets(ImmutableMultiset.of(
                        1, 1, 1
                ))
        );
        Assert.assertEquals(
                TernaryLogic.No,
                partitioningStrategy.isCanBePartedToSameSumSets(ImmutableMultiset.of(
                        3, 1, 1
                ))
        );
        Assert.assertEquals(
                TernaryLogic.Yes,
                partitioningStrategy.isCanBePartedToSameSumSets(ImmutableMultiset.of(
                        5, 3, 1, 5, 1
                ))
        );
        Assert.assertEquals(
                TernaryLogic.Yes,
                partitioningStrategy.isCanBePartedToSameSumSets(ImmutableMultiset.of(
                        9,
                        2, 7,
                        6, 1, 2,
                        8, 1
                ))
        );
    }

    @Test
    public void testGetDescription() throws Exception {
        Assert.assertEquals(
                "Brute force",
                partitioningStrategy.getDescription()
        );
    }
}
