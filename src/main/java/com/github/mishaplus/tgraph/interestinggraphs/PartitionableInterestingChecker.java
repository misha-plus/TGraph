package com.github.mishaplus.tgraph.interestinggraphs;

import com.github.mishaplus.tgraph.GraphMarks;
import com.github.mishaplus.tgraph.util.MyEdge;
import org.jgrapht.graph.DirectedPseudograph;

public class PartitionableInterestingChecker implements InterestingChecker {
    @Override
    public boolean isInteresting(DirectedPseudograph<Integer, MyEdge> graph, GraphMarks marks) {
        return marks.isPartitionable();
    }

    @Override
    public String filenamePrefix() {
        return "partitionable";
    }

    @Override
    public String getDescription() {
        return "partitionable";
    }
}
