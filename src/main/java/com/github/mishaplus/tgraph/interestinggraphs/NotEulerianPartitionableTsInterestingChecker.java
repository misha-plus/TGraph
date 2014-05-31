package com.github.mishaplus.tgraph.interestinggraphs;

import com.github.mishaplus.tgraph.GraphMarks;
import com.github.mishaplus.tgraph.MarkType;
import com.github.mishaplus.tgraph.util.MyEdge;
import org.jgrapht.graph.DirectedPseudograph;

public class NotEulerianPartitionableTsInterestingChecker implements InterestingChecker {
    @Override
    public boolean isInteresting(DirectedPseudograph<Integer, MyEdge> graph, GraphMarks marks) {
        return
                !marks.getBoolean(MarkType.isEulerian) &&
                 marks.isPartitionable() &&
                 marks.isTotallySynchronizable();

    }

    @Override
    public String filenamePrefix() {
        return "notEulerianPartitionableTS";
    }

    @Override
    public String getDescription() {
        return "!eulerian & partitionable & TS";
    }
}
