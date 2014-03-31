package com.github.mishaplus.tgraph.numbersets.strategies;

import com.google.common.collect.Maps;
import com.google.common.collect.Multiset;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Predicate;

/**
 * Created by michael on 31.03.14
 */
public class BruteForceStrategy implements PartitioningStrategy {
    @Override
    public TernaryLogic isCanBePartedToSameSumSets(Multiset<Integer> multiset) {
        int[] numbers = new int[multiset.size()];
        {
            int index = 0;
            for (int num: multiset)
                numbers[index++] = num;
        }
        int[] startMask = new int[numbers.length];
        Arrays.fill(startMask, 1);

        Predicate<int[]> isSameSumPartition = mask -> {
            Map<Integer, Integer> classesSum = Maps.newHashMap();
            for (int i = 0; i < mask.length; i++)
                addSum(classesSum, mask[i], numbers[i]);

            if (classesSum.size() == 1)
                return false;

            int someClassSum = classesSum.values().iterator().next();
            for (Map.Entry<Integer, Integer> part : classesSum.entrySet())
                if (part.getValue() != someClassSum)
                    return false;

            return true;
        };

        boolean bruted = bruteMask(startMask, 1, numbers.length, 0, isSameSumPartition);
        return bruted ? TernaryLogic.Yes : TernaryLogic.No;
    }

    private void addSum(Map<Integer, Integer> sum, int index, int value) {
        if (!sum.containsKey(index))
            sum.put(index, 0);
        sum.put(index, sum.get(index) + value);
    }

    private boolean bruteMask(
            int[] mask,
            int valueFrom,
            int valueTo,
            int depth,
            Predicate<int[]> checker
    ) {
        if (depth == mask.length)
            return checker.test(mask);

        for (int value = valueFrom; value <= valueTo; value++) {
            mask[depth] = value;
            if (bruteMask(mask, valueFrom, valueTo, depth+1, checker))
                return true;
        }
        return false;
    }

    @Override
    public String getDescription() {
        return "Brute force";
    }
}
