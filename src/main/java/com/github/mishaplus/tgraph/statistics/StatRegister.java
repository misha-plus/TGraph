package com.github.mishaplus.tgraph.statistics;

import com.github.mishaplus.tgraph.GraphMarks;
import com.github.mishaplus.tgraph.interestinggraphs.*;
import com.github.mishaplus.tgraph.util.MyEdge;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.tuple.Pair;
import org.jgrapht.graph.DirectedPseudograph;

import java.io.PrintWriter;
import java.util.Map;
import java.util.Set;

public class StatRegister {
    Set<InterestingChecker> registerInspectors = ImmutableSet.of(
            new AlwaysInteresting(),
            new MayIncreasedToEulerianWithFixDegreeOnlyOverMaxDegreeInterestingChecker(),
            new TotSyncPartitionableInterestingChecker(),
            new TotSyncWithIncColorsNotTotSyncHeuristicInterestingChecker(),
            new EulerianInterestingChecker(),
            new TotallySynchronizableInterestingChecker(),
            new MayBeIncreasedToEulerianWithFixedDegreeInterestingChecker(),
            new PartitionableInterestingChecker(),
            new NotEulerianAndNotPartitionableInterestingChecker()
    );

    Map<Pair<Integer, Integer>, Map<String, Integer>> statistics = Maps.newTreeMap();

    private void incrementPropertyCount(
            int vertexCount,
            int outDegree,
            String propertyName
    ) {
        Pair<Integer, Integer> vertexCountAndOutDegree = Pair.of(vertexCount, outDegree);
        if (!statistics.containsKey(vertexCountAndOutDegree)) {
            statistics.put(vertexCountAndOutDegree, Maps.newHashMap());
            Map<String, Integer> properties = statistics.get(vertexCountAndOutDegree);
            for (InterestingChecker inspector : registerInspectors)
                properties.put(inspector.getDescription(), 0);
        }

        Map<String, Integer> properties = statistics.get(vertexCountAndOutDegree);

        properties.put(propertyName, properties.get(propertyName) + 1);
    }

    public void registerUnique(
            int vertexCount,
            int outDegree,
            DirectedPseudograph<Integer, MyEdge> graph,
            GraphMarks marks
    ) {
        for (InterestingChecker inspector : registerInspectors) {
            if (inspector.isInteresting(graph, marks)) {
                incrementPropertyCount(vertexCount, outDegree, inspector.getDescription());
            }
        }
    }

    public void saveStatistics(PrintWriter out) {
        for (Map.Entry<Pair<Integer, Integer>, Map<String, Integer>> properties : statistics.entrySet()) {
            int vertexCount = properties.getKey().getLeft();
            int outDegree   = properties.getKey().getRight();
            out.printf("(%d, %d):\n", vertexCount, outDegree);
            for (Map.Entry<String, Integer> property : properties.getValue().entrySet()) {
                String propertyName = property.getKey();
                int propertyCount   = property.getValue();
                out.printf("    %s: %d\n", propertyName, propertyCount);
            }
            out.printf("\n\n");
        }
    }
}
