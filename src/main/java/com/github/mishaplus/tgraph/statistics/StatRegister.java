package com.github.mishaplus.tgraph.statistics;

import com.github.mishaplus.tgraph.GraphMarks;
import com.github.mishaplus.tgraph.interestinggraphs.PermutationsCountInBaseInterestingChecker;
import com.github.mishaplus.tgraph.interestinggraphs.*;
import com.github.mishaplus.tgraph.util.MyEdge;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.tuple.Pair;
import org.jgrapht.graph.DirectedPseudograph;

import java.io.PrintWriter;
import java.util.Comparator;
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
            new NotEulerianAndPartitionableInterestingChecker(),
            new NotEulerianPartitionableTsInterestingChecker(),
            new IntersectionInterestingChecker(
                    new TotallySynchronizableInterestingChecker(),
                    new MayBeIncreasedToEulerianWithFixedDegreeInterestingChecker(),
                    //new NegateInterestingChecker(new EulerianInterestingChecker()),
                    new NegateInterestingChecker(new PartitionableInterestingChecker())
            ),
            new IntersectionInterestingChecker(
                    new NegateInterestingChecker(
                            new TotallySynchronizableInterestingChecker()
                    ),
                    new NegateInterestingChecker(
                            new MayBeIncreasedToEulerianWithFixedDegreeInterestingChecker()
                    )
            ),
            new IntersectionInterestingChecker(
                    new NegateInterestingChecker(new EulerianInterestingChecker()),
                    new MayBeIncreasedToEulerianWithFixedDegreeInterestingChecker()
            ),

            new IntersectionInterestingChecker(
                    new TotallySynchronizableInterestingChecker(),
                    new PermutationsCountInBaseInterestingChecker(Comparison.GREATER, 2)
            ),
            new IntersectionInterestingChecker(
                    new TotallySynchronizableInterestingChecker(),
                    new PermutationsCountInBaseInterestingChecker(Comparison.GREATER, 3)
            ),
            new IntersectionInterestingChecker(
                    new NegateInterestingChecker(
                            new TotallySynchronizableInterestingChecker()
                    ),
                    new PermutationsCountInBaseInterestingChecker(Comparison.LESS, 2)
            ),

            new IntersectionInterestingChecker(
                    new TotallySynchronizableInterestingChecker(),
                    new PermutationsCountInBaseInterestingChecker(Comparison.EQUAL, 2)
            ),
            new IntersectionInterestingChecker(
                    new NegateInterestingChecker(
                            new TotallySynchronizableInterestingChecker()
                    ),
                    new PermutationsCountInBaseInterestingChecker(Comparison.EQUAL, 2)
            ),

            new IntersectionInterestingChecker(
                    new NegateInterestingChecker(
                            new TotallySynchronizableInterestingChecker()
                    ),
                    new NegateInterestingChecker(
                            new EulerianInterestingChecker()
                    ),
                    new PermutationsCountInBaseInterestingChecker(Comparison.GREATER, 2)
            ),

            new HaveVertexWithInDegreeLessThanGraphOutDegreeAndOneInput()
    );

    Map<Pair<Integer, Integer>, Map<String, Integer>> statistics = Maps.newTreeMap();
    Map<Pair<Integer, Integer>, Integer> count = Maps.newTreeMap();

    private void incrementPropertyCount(
            int vertexCount,
            int outDegree,
            String propertyName
    ) {
        Pair<Integer, Integer> vertexCountAndOutDegree = Pair.of(vertexCount, outDegree);
        if (!statistics.containsKey(vertexCountAndOutDegree)) {
            statistics.put(vertexCountAndOutDegree, Maps.newTreeMap());
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
        Pair<Integer, Integer> vertexCountAndOutDegree = Pair.of(vertexCount, outDegree);
        if (!count.containsKey(vertexCountAndOutDegree))
            count.put(vertexCountAndOutDegree, 0);
        count.put(vertexCountAndOutDegree, count.get(vertexCountAndOutDegree) + 1);
        for (InterestingChecker inspector : registerInspectors) {
            if (inspector.isInteresting(graph, marks)) {
                incrementPropertyCount(vertexCount, outDegree, inspector.getDescription());
            }
        }
    }

    public void saveStatistics(PrintWriter out, boolean lexicographicDirection) {
        Map<Pair<Integer, Integer>, Map<String, Integer>> orderedStatistics
                = Maps.newTreeMap(new Comparator<Pair<Integer, Integer>>() {
            @Override
            public int compare(Pair<Integer, Integer> a, Pair<Integer, Integer> b) {
                if (lexicographicDirection)
                    return a.compareTo(b);
                else
                    return swap(a).compareTo(swap(b));
            }

            <L, R> Pair<R, L> swap(Pair<L, R> pair) {
                return Pair.of(pair.getRight(), pair.getLeft());
            }
        });

        orderedStatistics.putAll(statistics);

        for (Map.Entry<Pair<Integer, Integer>, Map<String, Integer>> properties : orderedStatistics.entrySet()) {
            int vertexCount = properties.getKey().getLeft();
            int outDegree   = properties.getKey().getRight();
            Pair<Integer, Integer> vertexCountAndOutDegree = Pair.of(vertexCount, outDegree);
            int allGraphsCount = count.get(vertexCountAndOutDegree);

            out.printf("(%d, %d):\n", vertexCount, outDegree);
            for (Map.Entry<String, Integer> property : properties.getValue().entrySet()) {
                String propertyName = property.getKey();
                int propertyCount   = property.getValue();
                out.printf(
                        "    %s: %d (%.2f%%)\n",
                        propertyName,
                        propertyCount,
                        propertyCount*100.0/allGraphsCount
                );
            }
            out.printf("\n\n");
        }
    }
}
