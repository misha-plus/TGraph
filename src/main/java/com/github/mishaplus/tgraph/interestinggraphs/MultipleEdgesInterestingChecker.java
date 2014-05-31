package com.github.mishaplus.tgraph.interestinggraphs;

import com.github.mishaplus.tgraph.GraphMarks;
import com.github.mishaplus.tgraph.util.MyEdge;
import org.jgrapht.graph.DirectedPseudograph;

import java.util.Set;
import java.util.stream.Collectors;

public class MultipleEdgesInterestingChecker implements InterestingChecker {
    @Override
    public boolean isInteresting(DirectedPseudograph<Integer, MyEdge> graph, GraphMarks marks) {
        for (int v : graph.vertexSet()) {
            Set<Integer> targets = graph
                    .edgesOf(v)
                    .stream()
                    .<Integer>map(graph::getEdgeTarget)
                    .collect(Collectors.toSet());
            for (int target : targets)
                if (graph.getAllEdges(v, target).size() > 1)
                    return true;
        }
        return false;
    }

    @Override
    public String filenamePrefix() {
        return "multipleEdgesGraphs";
    }

    @Override
    public String getDescription() {
        return "multipleEdges";
    }
}
