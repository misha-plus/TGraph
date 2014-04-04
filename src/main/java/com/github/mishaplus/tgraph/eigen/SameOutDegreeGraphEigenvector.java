package com.github.mishaplus.tgraph.eigen;

import com.github.mishaplus.tgraph.Converter;
import com.github.mishaplus.tgraph.IntegerMatrix;
import com.github.mishaplus.tgraph.util.MyEdge;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.apache.commons.math3.util.ArithmeticUtils;
import org.jgrapht.graph.DirectedPseudograph;

import java.util.List;
import java.util.Set;

public class SameOutDegreeGraphEigenvector {
    private EigenvectorFinder eigenvectorFinder = new GaussEigenvectorFinderImpl();//new BruteForceEigenvectorFinder();

    public List<Integer> getFriedmanEigenvectorWithRelativelyPrimeComponents(
            DirectedPseudograph<Integer, MyEdge> g
    ) {
        Set<Integer> vertices = g.vertexSet();
        Preconditions.checkArgument(vertices.size() > 0);
        int degree = g.outDegreeOf(vertices.iterator().next());
        for (Integer vertex: vertices)
            Preconditions.checkArgument(g.outDegreeOf(vertex) == degree);

        IntegerMatrix intMatrix = Converter.toIntAdjArray(g);
        List<Integer> eigenvector = eigenvectorFinder.findPositiveEigenvector(intMatrix, degree);
        Preconditions.checkNotNull(eigenvector, "EigenvectorNotFound");

        return toRelativePrime(eigenvector);
    }

    private List<Integer> toRelativePrime(List<Integer> list) {
        int[] gcd = {0};
        list.forEach(elem -> {
            gcd[0] = ArithmeticUtils.gcd(gcd[0], elem);
        });

        return Lists.newArrayList(list.stream().map(elem -> elem/gcd[0]).iterator());
    }
}
