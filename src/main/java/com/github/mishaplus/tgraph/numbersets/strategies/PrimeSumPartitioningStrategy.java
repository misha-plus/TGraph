package com.github.mishaplus.tgraph.numbersets.strategies;

import com.google.common.collect.Multiset;
import org.apache.commons.math3.primes.Primes;

public class PrimeSumPartitioningStrategy implements PartitioningStrategy {
    @Override
    public TernaryLogic isCanBePartedToSameSumSets(Multiset<Integer> multiset) {
        boolean isMultisetHaveNonOne = false;
        for (int num: multiset)
            if (num != 1)
                isMultisetHaveNonOne = true;

        return isMultisetHaveNonOne && Primes.isPrime(sum(multiset))
                ? TernaryLogic.No : TernaryLogic.Unknown;
    }

    private int sum(Multiset<Integer> multiset) {
        return multiset.stream().reduce((a, b) -> a+b).get();
    }

    @Override
    public String getDescription() {
        return "Prime";
    }
}
