package com.github.mishaplus.tgraph.interestinggraphs;

import com.github.mishaplus.tgraph.GraphMarks;
import com.github.mishaplus.tgraph.PermutationCounter;
import com.github.mishaplus.tgraph.util.MyEdge;
import org.jgrapht.graph.DirectedPseudograph;

public class OverThanTwoPermutationsInterestingChecker implements InterestingChecker {
    private PermutationCounter permutationCounter = new PermutationCounter();

    @Override
    public boolean isInteresting(DirectedPseudograph<Integer, MyEdge> graph, GraphMarks marks) {
        return permutationCounter.count(graph) > 2;
    }

    @Override
    public String filenamePrefix() {
        return "overThanTwoPermutations";
    }

    @Override
    public String getDescription() {
        return "overThanTwoPermutations";
    }
}
