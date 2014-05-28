package com.github.mishaplus.tgraph.interestinggraphs;

import com.github.mishaplus.tgraph.GraphMarks;
import com.github.mishaplus.tgraph.MarkType;
import com.github.mishaplus.tgraph.util.MyEdge;
import org.jgrapht.graph.DirectedPseudograph;

public class TotSyncWithIncColorsNotTotSyncHeuristicInterestingChecker implements InterestingChecker {
    @Override
    public boolean isInteresting(DirectedPseudograph<Integer, MyEdge> graph, GraphMarks marks) {
        return
                marks.isTotallySynchronizable() &&
                marks.getBoolean(MarkType.isMayBeIncreasedToEulerianWithFixedDegree);
    }

    @Override
    public String filenamePrefix() {
        return "partOfTotSyncWithIncColorsNotTotSync";
    }
}
