package com.github.mishaplus.tgraph.interestinggraphs;

import com.github.mishaplus.tgraph.GraphMarks;
import com.github.mishaplus.tgraph.util.MyEdge;
import com.github.mishaplus.tgraph.util.Util;
import com.google.common.base.Preconditions;
import org.jgrapht.graph.DirectedPseudograph;

import java.util.Set;
import java.util.stream.Collectors;

public class HaveVertexWithInDegreeLessThanGraphOutDegreeAndOneInput implements InterestingChecker {
    @Override
    public boolean isInteresting(DirectedPseudograph<Integer, MyEdge> graph, GraphMarks marks) {
        Preconditions.checkArgument(Util.isGraphHaveSameDegree(graph));
        int graphVertexOutDegree = Util.getSomeDegree(graph);
        for (int v : graph.vertexSet()) {
            Set<MyEdge> incomingEdges = graph.incomingEdgesOf(v);
            Set<Integer> fromVertices = incomingEdges
                    .stream()
                    .<Integer>map(graph::getEdgeSource)
                    .collect(Collectors.toSet());
            if (fromVertices.size() == 1 && incomingEdges.size() < graphVertexOutDegree)
                return true;
        }
        return false;
    }

    @Override
    public String filenamePrefix() {
        return "haveVertexWithInDegreeLessThanGraphOutDegreeAndOneInput";
    }

    @Override
    public String getDescription() {
        return "haveVertexWithInDegreeLessThanGraphOutDegreeAndOneInput";
    }
}
