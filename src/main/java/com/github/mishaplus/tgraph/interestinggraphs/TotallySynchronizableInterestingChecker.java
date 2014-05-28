package com.github.mishaplus.tgraph.interestinggraphs;

import com.github.mishaplus.tgraph.GraphMarks;
import com.github.mishaplus.tgraph.MarkType;
import com.github.mishaplus.tgraph.util.MyEdge;
import org.jgrapht.graph.DirectedPseudograph;

public class TotallySynchronizableInterestingChecker implements InterestingChecker {
    @Override
    public boolean isInteresting(DirectedPseudograph<Integer, MyEdge> graph, GraphMarks marks) {
        return marks.getBoolean(MarkType.isTotallySynchronizable);
    }

    @Override
    public String filenamePrefix() {
        return "totallySynchronizable";
    }

    @Override
    public String getDescription() {
        return "totallySynchronizable";
    }
}
