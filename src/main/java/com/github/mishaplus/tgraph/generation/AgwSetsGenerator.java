package com.github.mishaplus.tgraph.generation;

import com.github.mishaplus.tgraph.equivalence.GraphIsomorphismRelation;
import com.github.mishaplus.tgraph.equivalence.SplitToRepresenters;
import com.github.mishaplus.tgraph.util.MyEdge;
import com.google.common.collect.Sets;
import org.jgrapht.graph.DirectedPseudograph;

import java.util.Set;

public class AgwSetsGenerator {
    private final int vertexCount;
    private final int outDegreeUpperBound;

    public AgwSetsGenerator(int vertexCount, int outDegreeUpperBound) {
        this.vertexCount = vertexCount;
        this.outDegreeUpperBound = outDegreeUpperBound;
    }

    public Set<DirectedPseudograph<Integer, MyEdge>> generateAll() {
        Set<DirectedPseudograph<Integer, MyEdge>> result = Sets.newHashSet();
        new AllAgwActionGenerator(vertexCount, outDegreeUpperBound, result::add).brute();
        return result;
    }

    public Set<DirectedPseudograph<Integer, MyEdge>> generateAllNonIsomophic() {
        SplitToRepresenters<DirectedPseudograph<Integer, MyEdge>> directedPseudographsSplitter
                = new SplitToRepresenters<>(new GraphIsomorphismRelation());
        return directedPseudographsSplitter.split(generateAll());
    }
}
