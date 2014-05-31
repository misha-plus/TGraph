package com.github.mishaplus.tgraph.permanent;

import com.github.mishaplus.tgraph.IntegerMatrix;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.iterators.PermutationIterator;

import java.util.List;

public class PermanentEvaluator {
    public int evaluate(IntegerMatrix matrix) {
        Preconditions.checkArgument(matrix.m == matrix.n);
        List<Integer> idPermutation = Lists.newArrayList();
        for (int i = 0; i < matrix.n; i++)
            idPermutation.add(i);

        int resultSum = 0;
        PermutationIterator<Integer> permutationIterator = new PermutationIterator<>(idPermutation);
        while (permutationIterator.hasNext()) {
            List<Integer> perm = Lists.newArrayList(permutationIterator.next());
            int curProduct = 1;
            for (int i = 0; i < matrix.n && curProduct != 0; i++) {
                curProduct *= matrix.matrix[i][perm.get(i)];
            }
            resultSum += curProduct;
        }
        return resultSum;
    }
}
