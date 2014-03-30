package com.github.mishaplus.tgraph.equivalence;

import com.github.mishaplus.tgraph.Converter;
import com.github.mishaplus.tgraph.IntegerMatrix;
import com.github.mishaplus.tgraph.util.MyEdge;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.iterators.PermutationIterator;
import org.jgrapht.graph.DirectedPseudograph;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GraphIsomorphismRelation
        implements EquivalenceRelation<DirectedPseudograph<Integer, MyEdge>> {
    @Override
    public boolean isEquivalent(
            DirectedPseudograph<Integer, MyEdge> a,
            DirectedPseudograph<Integer, MyEdge> b
    ) {
        /*return AdaptiveIsomorphismInspectorFactory
                .createIsomorphismInspector(a, b)
                .isIsomorphic();
                */
        IntegerMatrix aMatrix = Converter.toIntAdjArray(a);
        IntegerMatrix bMatrix = Converter.toIntAdjArray(b);

        if (aMatrix.n != bMatrix.n || aMatrix.m != bMatrix.m)
            return false;

        List<Integer> indices = Lists.newArrayList();
        for (int i = 0; i < aMatrix.n; i++)
            indices.add(i);

        Iterator<List<Integer>> permutations = new PermutationIterator<>(indices);

        while (permutations.hasNext()) {
            ArrayList<Integer> fastPermutation = Lists.newArrayList(permutations.next());
            IntegerMatrix permutated = aMatrix.swapRowsAndLines(fastPermutation::get);
            if (permutated.equals(bMatrix))
                return true;
        }

        return false;
    }
}
