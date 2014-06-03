package com.github.mishaplus.tgraph.interestinggraphs;

import com.github.mishaplus.tgraph.GraphMarks;
import com.github.mishaplus.tgraph.PermutationCounter;
import com.github.mishaplus.tgraph.util.MyEdge;
import com.google.common.collect.ImmutableMap;
import org.jgrapht.graph.DirectedPseudograph;

import java.util.Map;
import java.util.function.Function;

public class PermutationsCountInBaseInterestingChecker implements InterestingChecker {
    private final int threshold;
    private final Comparison comparison;
    private final PermutationCounter permutationCounter = new PermutationCounter();

    public PermutationsCountInBaseInterestingChecker(Comparison comparison, int threshold) {
        this.threshold = threshold;
        this.comparison = comparison;
    }

    @Override
    public boolean isInteresting(DirectedPseudograph<Integer, MyEdge> graph, GraphMarks marks) {
        Map<Comparison, Function<Integer, Boolean>> comparers = ImmutableMap.<Comparison, Function<Integer, Boolean>>of(
                Comparison.LESS,             (Integer count) -> count <  threshold,
                Comparison.LESS_OR_EQUAL,    (Integer count) -> count <= threshold,

                Comparison.GREATER,          (Integer count) -> count >  threshold,
                Comparison.GREATER_OR_EQUAL, (Integer count) -> count >= threshold,

                Comparison.EQUAL,            (Integer count) -> count == threshold
        );
        return comparers.get(comparison).apply(permutationCounter.count(graph));
    }

    @Override
    public String filenamePrefix() {
        return String.format("permutationsInBase%s%d", comparison, threshold);
    }

    @Override
    public String getDescription() {
        Map<Comparison, String> sign = ImmutableMap.of(
                Comparison.LESS,             "<",
                Comparison.LESS_OR_EQUAL,    "<=",

                Comparison.GREATER,          ">",
                Comparison.GREATER_OR_EQUAL, ">=",

                Comparison.EQUAL,            "=="
        );
        return String.format("[permutationsInBase %s %d]", sign.get(comparison), threshold);
    }
}
