package com.github.mishaplus.tgraph.interestinggraphs;

import com.github.mishaplus.tgraph.GraphMarks;
import com.github.mishaplus.tgraph.MarkType;
import com.github.mishaplus.tgraph.util.MyEdge;
import org.jgrapht.graph.DirectedPseudograph;

public class MayIncreasedToEulerianWithFixDegreeOnlyOverMaxDegreeInterestingChecker
        implements InterestingChecker {

    @Override
    public boolean isInteresting(DirectedPseudograph<Integer, MyEdge> graph, GraphMarks marks) {
        return
                marks.getBoolean(MarkType.isMayBeIncreasedToEulerianWithFixedDegree) &&
                !marks.getBoolean(MarkType.isMayBeIncreasedToEulerianWithFixedDegreeEqMaxDegree);
    }

    @Override
    public String filenamePrefix() {
        return "mayIncreasedToEulerianWithFixDegreeOnlyOverMaxDegree";
    }

    @Override
    public String getDescription() {
        return "mayBeIncreasedToEulerianWithFixedDegree & !mayBeIncreasedToEulerianWithFixedDegreeEqMaxDegree";
    }
}
