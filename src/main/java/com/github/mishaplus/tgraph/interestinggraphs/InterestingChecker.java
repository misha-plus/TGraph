package com.github.mishaplus.tgraph.interestinggraphs;

import com.github.mishaplus.tgraph.GraphMarks;
import com.github.mishaplus.tgraph.util.MyEdge;
import org.jgrapht.graph.DirectedPseudograph;

public interface InterestingChecker {
    boolean isInteresting(DirectedPseudograph<Integer, MyEdge> graph, GraphMarks marks);
    String filenamePrefix();
    String getDescription();
}
