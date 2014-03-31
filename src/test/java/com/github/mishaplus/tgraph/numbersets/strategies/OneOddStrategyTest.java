package com.github.mishaplus.tgraph.numbersets.strategies;

import com.google.common.collect.ImmutableMultiset;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by michael on 31.03.14
 */
public class OneOddStrategyTest {
    PartitioningStrategy partitioningStrategy = new OneOddStrategy();

    @Test
    public void testIsCanBePartedToSameSumSets() throws Exception {
        Assert.assertEquals(
                TernaryLogic.No,
                partitioningStrategy.isCanBePartedToSameSumSets(ImmutableMultiset.of(
                        1, 4, 8
                ))
        );
        Assert.assertEquals(
                TernaryLogic.No,
                partitioningStrategy.isCanBePartedToSameSumSets(ImmutableMultiset.of(
                        1
                ))
        );
        Assert.assertEquals(
                TernaryLogic.No,
                partitioningStrategy.isCanBePartedToSameSumSets(ImmutableMultiset.of(
                        4, 2, 2, 6, 7
                ))
        );
        Assert.assertEquals(
                TernaryLogic.Unknown,
                partitioningStrategy.isCanBePartedToSameSumSets(ImmutableMultiset.of(
                        4, 2, 2, 6, 1, 1
                ))
        );
    }

    @Test
    public void testGetDescription() throws Exception {
        Assert.assertThat(
                partitioningStrategy.getDescription(),
                Matchers.containsString("One odd")
        );
    }
}
