package com.github.mishaplus.tgraph.numbersets.strategies;

import com.google.common.collect.ImmutableMultiset;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by michael on 31.03.14
 */
public class OneNumberPartitioningStrategyTest {
    PartitioningStrategy partitioningStrategy = new OneNumberPartitioningStrategy();

    @Test
    public void testIsCouldNotBePartedToSameSumSets() throws Exception {
        Assert.assertEquals(
                TernaryLogic.No,
                partitioningStrategy.isCanBePartedToSameSumSets(ImmutableMultiset.of(
                        4
                ))
        );
        Assert.assertEquals(
                TernaryLogic.Unknown,
                partitioningStrategy.isCanBePartedToSameSumSets(ImmutableMultiset.of(
                        1, 1
                ))
        );
    }

    @Test
    public void testGetDescription() throws Exception {
        Assert.assertThat(
                partitioningStrategy.getDescription(),
                Matchers.anyOf(
                        Matchers.containsString("One number"),
                        Matchers.containsString("one number")
                )
        );
    }
}
