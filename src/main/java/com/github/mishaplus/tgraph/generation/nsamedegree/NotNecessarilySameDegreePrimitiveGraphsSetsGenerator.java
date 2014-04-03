package com.github.mishaplus.tgraph.generation.nsamedegree;

import com.github.mishaplus.tgraph.equivalence.GraphIsomorphismRelation;
import com.github.mishaplus.tgraph.equivalence.SplitToRepresenters;
import com.github.mishaplus.tgraph.generation.nsamedegree.NotNecessarilySameDegreePrimitiveGraphsActionGenerator;
import com.github.mishaplus.tgraph.util.MyEdge;
import com.google.common.collect.Sets;
import org.jgrapht.graph.DirectedPseudograph;

import java.util.Set;

public class NotNecessarilySameDegreePrimitiveGraphsSetsGenerator {
    private final int vertexCount;
    private final int outDegreeUpperBound;

    public NotNecessarilySameDegreePrimitiveGraphsSetsGenerator(int vertexCount, int outDegreeUpperBound) {
        this.vertexCount = vertexCount;
        this.outDegreeUpperBound = outDegreeUpperBound;
    }

    public Set<DirectedPseudograph<Integer, MyEdge>> generateAll() {
        Set<DirectedPseudograph<Integer, MyEdge>> result = Sets.newHashSet();
        new NotNecessarilySameDegreePrimitiveGraphsActionGenerator(
                vertexCount,
                outDegreeUpperBound,
                result::add
        ).bruteAll();
        return result;
    }

    public Set<DirectedPseudograph<Integer, MyEdge>> generateAllNonIsomophic() {
        SplitToRepresenters<DirectedPseudograph<Integer, MyEdge>> directedPseudographsSplitter
                = new SplitToRepresenters<>(new GraphIsomorphismRelation());
        Set<DirectedPseudograph<Integer, MyEdge>> graphs = generateAll();
        return directedPseudographsSplitter.split(graphs);
    }
}
