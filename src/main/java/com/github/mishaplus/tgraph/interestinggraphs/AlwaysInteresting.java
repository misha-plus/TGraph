package com.github.mishaplus.tgraph.interestinggraphs;

import com.github.mishaplus.tgraph.GraphMarks;
import com.github.mishaplus.tgraph.util.MyEdge;
import org.jgrapht.graph.DirectedPseudograph;

public class AlwaysInteresting implements InterestingChecker {
    @Override
    public boolean isInteresting(DirectedPseudograph<Integer, MyEdge> graph, GraphMarks marks) {
        return true;
    }

    @Override
    public String filenamePrefix() {
        return "graphs";
    }

    @Override
    public String getDescription() {
        return "All graphs";
    }
}
