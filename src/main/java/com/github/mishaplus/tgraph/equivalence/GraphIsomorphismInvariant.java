package com.github.mishaplus.tgraph.equivalence;

import com.github.mishaplus.tgraph.eigen.EigenvectorNotFoundException;
import com.github.mishaplus.tgraph.eigen.SameOutDegreeGraphEigenvector;
import com.github.mishaplus.tgraph.util.MyEdge;
import com.github.mishaplus.tgraph.util.Util;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.ListUtils;
import org.jgrapht.graph.DirectedPseudograph;

import java.util.Collections;
import java.util.List;

/**
 * Created by michael on 03.04.14
 */
public class GraphIsomorphismInvariant
        implements EquivalenceInvariant<DirectedPseudograph<Integer, MyEdge>> {

    @Override
    public List<?> getInvariant(DirectedPseudograph<Integer, MyEdge> g) {
        return ListUtils.union(inputDegrees(g), friedmanEigenvector(g));
    }

    private <V, E> List<Integer> inputDegrees(DirectedPseudograph<V, E> g) {
        List<Integer> result = Lists.newArrayList();
        g.vertexSet().forEach(v -> result.add(g.inDegreeOf(v)));
        Collections.sort(result);
        return result;
    }

    private List<Integer> friedmanEigenvector(DirectedPseudograph<Integer, MyEdge> g) {
        List<Integer> result = null;
        if (Util.isGraphHaveSameDegree(g))
            result = new SameOutDegreeGraphEigenvector()
                    .getFriedmanEigenvectorWithRelativelyPrimeComponents(g);
        if (result == null) {
            List<Integer> tmp = Lists.newArrayList();
            g.vertexSet().forEach(v -> tmp.add(0));
            return tmp;
        } else {
            Collections.sort(result);
            return result;
        }
    }
}
