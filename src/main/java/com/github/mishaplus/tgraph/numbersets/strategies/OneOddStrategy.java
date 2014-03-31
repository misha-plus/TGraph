package com.github.mishaplus.tgraph.numbersets.strategies;

import com.google.common.collect.Multiset;

public class OneOddStrategy implements PartitioningStrategy {
    @Override
    public TernaryLogic isCanBePartedToSameSumSets(Multiset<Integer> multiset) {
        int oddCount = 0;
        for (int num: multiset)
            if (num % 2 == 1)
                oddCount++;
        return oddCount == 1 ? TernaryLogic.No : TernaryLogic.Unknown;
    }

    @Override
    public String getDescription() {
        return "One odd";
    }
}
