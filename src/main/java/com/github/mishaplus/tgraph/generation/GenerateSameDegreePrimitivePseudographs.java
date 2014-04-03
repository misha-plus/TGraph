package com.github.mishaplus.tgraph.generation;

import com.github.mishaplus.tgraph.equivalence.GraphIsomorphismInvariant;
import com.github.mishaplus.tgraph.equivalence.GraphIsomorphismRelation;
import com.github.mishaplus.tgraph.equivalence.SplitToRepresenters;
import com.github.mishaplus.tgraph.generation.nsamedegree.NotNecessarilySameDegreePrimitiveGraphsSetsGenerator;
import com.github.mishaplus.tgraph.util.MyEdge;
import com.github.mishaplus.tgraph.util.Util;
import com.google.common.collect.Sets;
import org.jgrapht.graph.DirectedPseudograph;

import java.util.Set;

public class GenerateSameDegreePrimitivePseudographs {
    private final int vertexCount;
    private final int vertexDegree;

    public GenerateSameDegreePrimitivePseudographs(int vertexCount, int vertexDegree) {
        this.vertexCount = vertexCount;
        this.vertexDegree = vertexDegree;
    }

    public Set<DirectedPseudograph<Integer, MyEdge>> generateAllNonIsomorphic() {
        return fast();
    }

    private Set<DirectedPseudograph<Integer, MyEdge>> slow() {
        Set<DirectedPseudograph<Integer, MyEdge>> generatedNotExceedDegree
                = new NotNecessarilySameDegreePrimitiveGraphsSetsGenerator(
                vertexCount, vertexDegree
        ).generateAll();

        Set<DirectedPseudograph<Integer, MyEdge>> nonIsomorphic = new SplitToRepresenters<>(
                new GraphIsomorphismRelation()
        ).split(generatedNotExceedDegree);

        return Sets.newHashSet(nonIsomorphic
                        .stream()
                        .filter(g -> Util.isGraphHaveDegree(g, vertexDegree))
                        .iterator()
        );
    }

    private Set<DirectedPseudograph<Integer, MyEdge>> fast() {
        Set<DirectedPseudograph<Integer, MyEdge>> generatedSameDegree = Sets.newHashSet();

        long startMillis = System.currentTimeMillis();
        System.out.println(startMillis);

        new SameDegreeGraphBruteForcer(
                vertexCount,
                vertexDegree,
                g -> {
                    Set<DirectedPseudograph<Integer, MyEdge>> s = generatedSameDegree;
                    if (PrimitiveGraphInspector.isPrimitive(g))
                        generatedSameDegree.add(g);
                }
        ).brute();

        long endMillis = System.currentTimeMillis();
        System.out.println(endMillis);
        System.out.println(endMillis - startMillis);

        Set<DirectedPseudograph<Integer, MyEdge>> nonIsomorphic = new SplitToRepresenters<>(
                new GraphIsomorphismRelation(),
                new GraphIsomorphismInvariant()
        ).split(generatedSameDegree);

        return nonIsomorphic;
    }
}
