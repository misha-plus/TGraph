package com.github.mishaplus.tgraph.interestinggraphs;

import com.github.mishaplus.tgraph.GraphMarks;
import com.github.mishaplus.tgraph.util.MyEdge;
import org.jgrapht.graph.DirectedPseudograph;

public class NegateInterestingChecker implements InterestingChecker {
    private final InterestingChecker checker;

    public NegateInterestingChecker(InterestingChecker checker) {
        this.checker = checker;
    }

    @Override
    public boolean isInteresting(DirectedPseudograph<Integer, MyEdge> graph, GraphMarks marks) {
        return !checker.isInteresting(graph, marks);
    }

    @Override
    public String filenamePrefix() {
        return String.format("not(%s)", checker.filenamePrefix());
    }

    @Override
    public String getDescription() {
        String checkerDescription = checker.getDescription();
        if (checkerDescription.matches("[!A-Za-z0-9]+"))
            return "!" + checkerDescription;
        else
            return String.format("!(%s)", checkerDescription);
    }
}
