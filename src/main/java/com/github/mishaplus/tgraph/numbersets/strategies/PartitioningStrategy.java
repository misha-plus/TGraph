package com.github.mishaplus.tgraph.numbersets.strategies;

import com.google.common.collect.Multiset;

public interface PartitioningStrategy {
    TernaryLogic isCanBePartedToSameSumSets(Multiset<Integer> multiset);
    String getDescription();
}
