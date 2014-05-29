package com.github.mishaplus.tgraph.interestinggraphs;

import com.github.mishaplus.tgraph.GraphMarks;
import com.github.mishaplus.tgraph.MarkType;
import com.github.mishaplus.tgraph.util.MyEdge;
import org.jgrapht.graph.DirectedPseudograph;

public class NotEulerianAndPartitionableInterestingChecker implements InterestingChecker {
    @Override
    public boolean isInteresting(DirectedPseudograph<Integer, MyEdge> graph, GraphMarks marks) {
        return marks.isPartitionable() && !marks.getBoolean(MarkType.isEulerian);
    }

    @Override
    public String filenamePrefix() {
        return "notEulerianAndPartitionable";
    }

    @Override
    public String getDescription() {
        return "!eulerian && partitionable";
    }
}
