package com.github.mishaplus.tgraph.interestinggraphs;

import com.github.mishaplus.tgraph.GraphMarks;
import com.github.mishaplus.tgraph.util.MyEdge;
import org.jgrapht.graph.DirectedPseudograph;

public class TotSyncPartitionableInterestingChecker implements InterestingChecker {
    @Override
    public boolean isInteresting(DirectedPseudograph<Integer, MyEdge> graph, GraphMarks marks) {
        return marks.isPartitionable() && marks.isTotallySynchronizable();
    }

    @Override
    public String filenamePrefix() {
        return "totSyncPartitionable";
    }
}
