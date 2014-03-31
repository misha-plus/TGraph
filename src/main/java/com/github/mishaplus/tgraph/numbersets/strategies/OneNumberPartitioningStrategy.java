package com.github.mishaplus.tgraph.numbersets.strategies;

import com.google.common.collect.Multiset;

public class OneNumberPartitioningStrategy implements PartitioningStrategy {
    @Override
    public TernaryLogic isCanBePartedToSameSumSets(Multiset<Integer> multiset) {
        return multiset.size() == 1 ? TernaryLogic.No : TernaryLogic.Unknown;
    }

    @Override
    public String getDescription() {
        return "One number";
    }
}
