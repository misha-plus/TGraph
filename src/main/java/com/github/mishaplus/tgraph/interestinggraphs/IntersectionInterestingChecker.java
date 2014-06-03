package com.github.mishaplus.tgraph.interestinggraphs;

import com.github.mishaplus.tgraph.GraphMarks;
import com.github.mishaplus.tgraph.util.MyEdge;
import com.google.common.collect.ImmutableList;
import org.jgrapht.graph.DirectedPseudograph;

import java.util.List;

public class IntersectionInterestingChecker implements InterestingChecker {
    private final List<InterestingChecker> checkers;

    public IntersectionInterestingChecker(InterestingChecker... checkers) {
        this.checkers = ImmutableList.copyOf(checkers);
    }

    @Override
    public boolean isInteresting(DirectedPseudograph<Integer, MyEdge> graph, GraphMarks marks) {
        for (InterestingChecker checker : checkers)
            if (!checker.isInteresting(graph, marks))
                return false;
        return true;
    }

    @Override
    public String filenamePrefix() {
        StringBuilder result = new StringBuilder();
        for (InterestingChecker checker : checkers) {
            if (result.length() > 0)
                result.append("and");
            result.append(String.format("(%s)", checker.filenamePrefix()));
        }
        return result.toString();
    }

    @Override
    public String getDescription() {
        StringBuilder result = new StringBuilder();
        for (InterestingChecker checker : checkers) {
            if (result.length() > 0)
                result.append(" & ");
            String checkerDescription = checker.getDescription();
            if (checkerDescription.matches("!*([A-Za-z0-9]+|(\\[.*\\]))"))
                result.append(checkerDescription);
            else
                result.append(String.format("(%s)", checkerDescription));
        }
        return result.toString();
    }
}
