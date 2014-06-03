package com.github.mishaplus.tgraph;

import com.github.mishaplus.tgraph.automata.Automata;
import com.github.mishaplus.tgraph.automata.coloring.TotallySynchronizationBruteChecker;
import com.github.mishaplus.tgraph.diophantineequation.FixedDegreeEulerianFinder;
import com.github.mishaplus.tgraph.eigen.SameOutDegreeGraphEigenvector;
import com.github.mishaplus.tgraph.interestinggraphs.*;
import com.github.mishaplus.tgraph.numbersets.strategies.BruteForceStrategy;
import com.github.mishaplus.tgraph.numbersets.strategies.TernaryLogic;
import com.github.mishaplus.tgraph.statistics.StatRegister;
import com.github.mishaplus.tgraph.util.DirectedPseudographCreator;
import com.github.mishaplus.tgraph.util.MyEdge;
import com.google.common.base.Objects;
import com.google.common.collect.*;
import org.apache.commons.lang3.tuple.Pair;
import org.jgrapht.graph.DirectedPseudograph;

import java.io.*;
import java.util.*;

class CacheEntry {
    Map<DirectedPseudograph<Integer, MyEdge>, GraphMarks> marked = Maps.newHashMap();
    Multimap<GraphMarks, DirectedPseudograph<Integer, MyEdge>> invMarked
            = ArrayListMultimap.create();

    Map<DirectedPseudograph<Integer, MyEdge>, List<Integer>> toEigen = Maps.newHashMap();
}

class SynchronizationEntry {
    final boolean isSynchronizable;
    final TernaryLogic isPartitionable;
    final boolean isMayBeIncreasedToEulerianWithFixedDegree;
    final boolean isMayBeIncreasedToEulerianWithFixedDegreeEqMaxDegree;

    SynchronizationEntry(
            boolean isSynchronizable,
            TernaryLogic isPartitionable,
            boolean isMayBeIncreasedToEulerianWithFixedDegree,
            boolean isMayBeIncreasedToEulerianWithFixedDegreeEqMaxDegree
    ) {
        this.isSynchronizable = isSynchronizable;
        this.isPartitionable  = isPartitionable;
        this.isMayBeIncreasedToEulerianWithFixedDegree
                = isMayBeIncreasedToEulerianWithFixedDegree;
        this.isMayBeIncreasedToEulerianWithFixedDegreeEqMaxDegree
                = isMayBeIncreasedToEulerianWithFixedDegreeEqMaxDegree;
    }

    @Override
    public String toString() {
        return String.format(
                "isTS:%5s isPartitionable:%3s isMayBeIncreasedToEulerianWithFixedDegree:%5s " +
                        "isMayBeIncreasedToEulerianWithFixedDegreeEqMaxDegree:%5s",
                isSynchronizable,
                isPartitionable,
                isMayBeIncreasedToEulerianWithFixedDegree,
                isMayBeIncreasedToEulerianWithFixedDegreeEqMaxDegree
        );
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(
                isPartitionable,
                isSynchronizable,
                isMayBeIncreasedToEulerianWithFixedDegree,
                isMayBeIncreasedToEulerianWithFixedDegreeEqMaxDegree
        );
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof SynchronizationEntry))
            return false;

        SynchronizationEntry other = (SynchronizationEntry) obj;
        return isSynchronizable == other.isSynchronizable
                && isPartitionable == other.isPartitionable
                && isMayBeIncreasedToEulerianWithFixedDegree
                        == other.isMayBeIncreasedToEulerianWithFixedDegree
                && isMayBeIncreasedToEulerianWithFixedDegreeEqMaxDegree
                        == other.isMayBeIncreasedToEulerianWithFixedDegreeEqMaxDegree;
    }
}

public class App {
    Map<Pair<Integer, Integer>, CacheEntry> cache = new HashMap<>();
    Map<DirectedPseudograph<Integer, MyEdge>, Boolean> isSynchronizableCache = new HashMap<>();
    StatRegister statRegister = new StatRegister();

    public void initIsSyncCache() throws FileNotFoundException {
        File isTSFile = new File("properties/isTS.txt");
        if (!isTSFile.exists())
            return;
        Scanner in = new Scanner(isTSFile);
        while (in.hasNext()) {
            String graphRepresentation = in.nextLine();
            boolean isTS = Boolean.valueOf(in.nextLine());
            isSynchronizableCache.put(DirectedPseudographCreator.fromString(graphRepresentation), isTS);
        }
        in.close();
    }

    public void saveIsSyncCache() throws FileNotFoundException {
        PrintWriter out = new PrintWriter("properties/isTS.txt");
        for (Map.Entry<DirectedPseudograph<Integer, MyEdge>, Boolean> cacheEntry : isSynchronizableCache.entrySet()) {
            out.println(cacheEntry.getKey());
            out.println(cacheEntry.getValue());
        }
        out.flush();
        out.close();
    }

    public void saveStatistics() throws FileNotFoundException {
        {
            PrintWriter out = new PrintWriter("statistics1order.txt");
            statRegister.saveStatistics(out, true);
            out.flush();
            out.close();
        }
        {
            PrintWriter out = new PrintWriter("statistics2order.txt");
            statRegister.saveStatistics(out, false);
            out.flush();
            out.close();
        }
    }

    public static void main(String[] args) throws Exception {
        App app = new App();
        PrintWriter out = new PrintWriter(System.out);
        //app.tmp2();
        //app.tmpShow();

        app.generateGraphsFiles();
        app.initIsSyncCache();

        Set<InterestingChecker> interestingCheckers = ImmutableSet.of(
                new TotSyncPartitionableInterestingChecker(),
                new TotSyncWithIncColorsNotTotSyncHeuristicInterestingChecker(),
                new MayIncreasedToEulerianWithFixDegreeOnlyOverMaxDegreeInterestingChecker(),
                new NotEulerianAndPartitionableInterestingChecker(),
                new IntersectionInterestingChecker(
                        new NegateInterestingChecker(
                                new EulerianInterestingChecker()
                        ),
                        new MayBeIncreasedToEulerianWithFixedDegreeInterestingChecker()
                )

        );

        //noinspection Convert2streamapi
        for (Map.Entry<Integer, Integer> vertexCountToDegree : generated.entries()) {
            int vertexCount = vertexCountToDegree.getKey();
            int outDegree = vertexCountToDegree.getValue();
            if (outDegree <= 5) {
                //app.run(vertexCountToDegree.getKey(), vertexCountToDegree.getValue(), out);
                //app.run(3, 3, false, out);
                app.markedGraphsSaver(vertexCount, outDegree);
                app.markedGraphsSaverWithNonSyncColorings(vertexCount, outDegree);
                for (InterestingChecker interestingChecker : interestingCheckers)
                    app.interestingGraphSaver(vertexCount, outDegree, interestingChecker, false);
            }
            if (outDegree == 2) {
                app.interestingGraphSaver(vertexCount, outDegree, new OverThanTwoPermutationsInterestingChecker(), false);
                app.interestingGraphSaver(vertexCount, outDegree, new TotallySynchronizableInterestingChecker(), false);
                app.interestingGraphSaver(vertexCount, outDegree, new IntersectionInterestingChecker(
                        new NegateInterestingChecker(
                                new TotallySynchronizableInterestingChecker()
                        ),
                        new NegateInterestingChecker(
                                new EulerianInterestingChecker()
                        ),
                        new PermutationsCountInBaseInterestingChecker(Comparison.GREATER, 2)
                ), true);
            }
        }

        app.learnStatistics();
        app.saveStatistics();

        //app.run(2, 2, new AlwaysInteresting(), false, out);

        out.flush();
        out.close();

        app.saveIsSyncCache();

    }

    public void learnStatistics() throws Exception {
        for (Map.Entry<Pair<Integer, Integer>, CacheEntry> storedInCache : cache.entrySet()) {
            int vertexCount = storedInCache.getKey().getLeft();
            int outDegree   = storedInCache.getKey().getRight();
            CacheEntry cacheEntry = storedInCache.getValue();
            for (Map.Entry<DirectedPseudograph<Integer, MyEdge>, GraphMarks> graphsToMarks : cacheEntry.marked.entrySet()) {
                statRegister.registerUnique(vertexCount, outDegree, graphsToMarks.getKey(), graphsToMarks.getValue());
            }
        }
    }

    public void interestingGraphSaver(
            int vertexCount,
            int outDegree,
            InterestingChecker interestingChecker,
            boolean isSaveColorings
    ) throws Exception {
        PrintWriter out = new PrintWriter(String.format(
                "interesting/%s(%d,%d).txt",
                interestingChecker.filenamePrefix(),
                vertexCount,
                outDegree
        ));

        run(vertexCount, outDegree, interestingChecker, isSaveColorings, out);
        out.flush();
        out.close();
    }

    public void markedGraphsSaver(int vertexCount, int outDegree) throws Exception {
        PrintWriter out = new PrintWriter(String.format(
                "markedGraphs/marked(%d,%d).txt",
                vertexCount,
                outDegree
        ));

        run(vertexCount, outDegree, new AlwaysInteresting(), false, out);
        out.flush();
        out.close();
    }

    public void markedGraphsSaverWithNonSyncColorings(
            int vertexCount,
            int outDegree
    ) throws Exception {
        PrintWriter out = new PrintWriter(String.format(
                "markedWithColorings/markedWithColorings(%d,%d).txt",
                vertexCount,
                outDegree
        ));

        run(vertexCount, outDegree, new AlwaysInteresting(), true, out);
        out.flush();
        out.close();
    }

    public void run() throws Exception {

    }

    static Multimap<Integer, Integer> generated = ImmutableMultimap.<Integer, Integer>builder()
            .putAll(2, ImmutableList.of(2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12))
            .putAll(3, ImmutableList.of(2, 3, 4/*, 5*/))
            .putAll(4, ImmutableList.of(2, 3))
            .put(5, 2)
            .put(6, 2)
            .build();

    public void generateGraphsFiles() throws IOException {
        for (Map.Entry<Integer, Integer> vertexCountToDegree : generated.entries())
            FilesGenerator.generateIfNotHave(
                    vertexCountToDegree.getKey(),
                    vertexCountToDegree.getValue()
            );

        /*
        for (int d : ImmutableList.of(2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12))
            FilesGenerator.generateIfNotHave(2, d);

        FilesGenerator.generateIfNotHave(3, 2);
        FilesGenerator.generateIfNotHave(3, 3);
        FilesGenerator.generateIfNotHave(3, 4);
        FilesGenerator.generateIfNotHave(3, 5);

        FilesGenerator.generateIfNotHave(4, 2);
        FilesGenerator.generateIfNotHave(4, 3);

        FilesGenerator.generateIfNotHave(5, 2);
        */
    }

    public void tmp2() throws Exception {
        String gString = "G:([1, 2, 3], [(1->2^1)=(1,2), (1->3^1)=(1,3), (2->2^1)=(2,2)," +
                " (2->3^1)=(2,3), (3->1^1)=(3,1), (3->2^1)=(3,2)]) " +
                "eigen:[1, 3, 2] {isTS:false isPartitionable:Yes} isGHaveMultipleEdges:false";
        DirectedPseudograph<Integer, MyEdge> g = DirectedPseudographCreator.fromString(gString);
        TotallySynchronizationBruteChecker.findAllNonSyncColorings(g).forEach(System.out::println);
    }

    public void tmpShow() throws Exception {
        String a = "G:([1, 5, 2, 4, 3], [(1->1^1)=(1,1), (1->5^1)=(1,5), (2->2^1)=(2,2)," +
                " (2->4^1)=(2,4), (3->1^1)=(3,1), (3->3^1)=(3,3), (4->3^1)=(4,3), " +
                "(4->5^1)=(4,5), (5->1^1)=(5,1), (5->2^1)=(5,2)]) " +
                "eigen:[3, 2, 1, 1, 2] {isTS:true isPartitionable:Yes} isGHaveMultipleEdges:false";
        String b = "G:([1, 4, 5, 2, 3], [(1->4^1)=(1,4), (1->5^1)=(1,5), (2->1^1)=(2,1)," +
                " (2->2^1)=(2,2), (3->1^1)=(3,1), (3->3^1)=(3,3), (4->2^1)=(4,2), (4->4^1)=(4,4), " +
                "(5->2^1)=(5,2), (5->3^1)=(5,3)]) eigen:[2, 3, 1, 2, 1]" +
                " {isTS:true isPartitionable:Yes} isGHaveMultipleEdges:false";
        String c = "G:([1, 3, 4, 2, 5], [(1->3^1)=(1,3), (1->4^1)=(1,4), (2->3^1)=(2,3), " +
                "(2->5^1)=(2,5), (3->1^1)=(3,1), (3->2^1)=(3,2), (4->2^1)=(4,2), (4->3^1)=(4,3), " +
                "(5->1^1)=(5,1), (5->3^1)=(5,3)]) eigen:[2, 2, 3, 1, 1] " +
                "{isTS:true isPartitionable:Yes} isGHaveMultipleEdges:false";
        DirectedPseudograph<Integer, MyEdge> g = DirectedPseudographCreator.fromString(
                a
        );

        Shower.show(g);
    }

    public boolean getCachedTotSyncProperty(
            DirectedPseudograph<Integer, MyEdge> graph
    ) {
        if (isSynchronizableCache.containsKey(graph))
            return isSynchronizableCache.get(graph);
        else {
            boolean result = TotallySynchronizationBruteChecker.checkTS(graph);
            isSynchronizableCache.put(graph, result);
            return result;
        }
    }

    public void run(
            int vertexCount,
            int outDegree,
            InterestingChecker interestingChecker,
            boolean isPrintNonSyncColorings,
            PrintWriter out
    ) throws Exception {
        Set<DirectedPseudograph<Integer, MyEdge>> generated = FilesGenerator.getGraphsFromFile(
                vertexCount, outDegree
        );

        CacheEntry cacheEntry = cache.containsKey(Pair.of(vertexCount, outDegree))
                ? cache.get(Pair.of(vertexCount, outDegree))
                : new CacheEntry();
        Map<DirectedPseudograph<Integer, MyEdge>, GraphMarks> marked = cacheEntry.marked;
        Multimap<GraphMarks, DirectedPseudograph<Integer, MyEdge>> invMarked = cacheEntry.invMarked;
        Map<DirectedPseudograph<Integer, MyEdge>, List<Integer>> toEigen = cacheEntry.toEigen;

        int progressIdx = 0;
        if (marked.isEmpty())
            for (DirectedPseudograph<Integer, MyEdge> g: generated) {
                progressIdx++;
                List<Integer> eigenvector = new SameOutDegreeGraphEigenvector()
                        .getFriedmanEigenvectorWithRelativelyPrimeComponents(g);

                GraphMarks marks = new GraphMarks();

                marks.setMark(
                        MarkType.isPartitionable,
                        new BruteForceStrategy()
                                .isCanBePartedToSameSumSets(ImmutableMultiset.copyOf(eigenvector))
                );

                marks.setMark(
                        MarkType.isTotallySynchronizable,
                        getCachedTotSyncProperty(g)
                );

                int maxDegree = 0;
                for (int v : g.vertexSet()) {
                    maxDegree = Math.max(maxDegree, g.outDegreeOf(v));
                    maxDegree = Math.max(maxDegree, g.inDegreeOf(v));
                }

                marks.setMark(
                        MarkType.isMayBeIncreasedToEulerianWithFixedDegreeEqMaxDegree,
                        new FixedDegreeEulerianFinder(g, g.edgeSet().size()).isSolveable()
                );

                marks.setMark(
                        MarkType.isMayBeIncreasedToEulerianWithFixedDegree,
                        new FixedDegreeEulerianFinder(g, maxDegree).isSolveable()
                );

                marks.setMark(
                        MarkType.isEulerian,
                        new EulerianInspector<Integer, MyEdge>().isGraphEulerianCycle(g)
                );

                toEigen.put(g, eigenvector);
                marked.put(g, marks);
                invMarked.put(marks, g);
            }

        cache.put(Pair.of(vertexCount, outDegree), cacheEntry);

        for (Map.Entry<GraphMarks, DirectedPseudograph<Integer, MyEdge>> syncClass : invMarked.entries()) {
            GraphMarks graphMarks = syncClass.getKey();
            DirectedPseudograph<Integer, MyEdge> g = syncClass.getValue();

            if (interestingChecker.isInteresting(g, graphMarks)) {
                out.printf(
                        "G:%s eigen:%s %s permutations:%d\n",
                        g,
                        toEigen.get(g),
                        graphMarks,
                        new PermutationCounter().count(g)
                );

                //if (synchronizationEntry.equals(new SynchronizationEntry(true, TernaryLogic.Yes)))
                //    Shower.show(g);

                /*if (!synchronizationEntry.isSynchronizable && !isGHaveMultiEdge)
                Shower.show(g);*/
                if (!graphMarks.isTotallySynchronizable() && isPrintNonSyncColorings) {
                    out.printf("[");
                    Set<Automata<Integer, Character>> nonSyncColorings
                             = TotallySynchronizationBruteChecker.findAllNonSyncNonCharIsomorphicColorings(g);
                    out.print(nonSyncColorings);
                    out.printf("]\n\n");

                    //Shower.show(nonSyncColoringsCache.iterator().next());
                }
            }

        }

        //Shower.show(g);
    }

    public void runOld(
            int vertexCount,
            int outDegree,
            boolean isPrintNonSyncColorings,
            PrintWriter out
    ) throws Exception {
        Set<DirectedPseudograph<Integer, MyEdge>> generated = FilesGenerator.getGraphsFromFile(
                vertexCount, outDegree
        );

        Map<DirectedPseudograph<Integer, MyEdge>, SynchronizationEntry> marked = Maps.newHashMap();
        Multimap<SynchronizationEntry, DirectedPseudograph<Integer, MyEdge>> invMarked
                = ArrayListMultimap.create();

        Map<DirectedPseudograph<Integer, MyEdge>, List<Integer>> toEigen = Maps.newHashMap();

        for (DirectedPseudograph<Integer, MyEdge> g: generated) {
            List<Integer> eigenvector = new SameOutDegreeGraphEigenvector()
                    .getFriedmanEigenvectorWithRelativelyPrimeComponents(g);

            TernaryLogic isCanBePartitioned = new BruteForceStrategy()
                    .isCanBePartedToSameSumSets(ImmutableMultiset.copyOf(eigenvector));

            boolean isTotallySynchronizable = TotallySynchronizationBruteChecker.checkTS(g);

            int maxDegree = 0;
            for (int v : g.vertexSet()) {
                maxDegree = Math.max(maxDegree, g.outDegreeOf(v));
                maxDegree = Math.max(maxDegree, g.inDegreeOf(v));
            }

            SynchronizationEntry syncEntry = new SynchronizationEntry(
                    isTotallySynchronizable,
                    isCanBePartitioned,
                    new FixedDegreeEulerianFinder(g, g.edgeSet().size()).isSolveable(),
                    new FixedDegreeEulerianFinder(g, maxDegree).isSolveable()
            );

            toEigen.put(g, eigenvector);
            marked.put(g, syncEntry);
            invMarked.put(syncEntry, g);
        }

        for (Map.Entry<SynchronizationEntry, DirectedPseudograph<Integer, MyEdge>> syncClass : invMarked.entries()) {
            SynchronizationEntry synchronizationEntry = syncClass.getKey();
            DirectedPseudograph<Integer, MyEdge> g = syncClass.getValue();

            boolean isGHaveMultiEdge = false;

            for (int v : g.vertexSet()) {
                Set<Integer> to = Sets.newHashSet();
                //noinspection Convert2streamapi
                for (MyEdge edge : g.outgoingEdgesOf(v))
                    to.add(g.getEdgeTarget(edge));
                if (to.size() == 1)
                    isGHaveMultiEdge = true;
            }

            out.printf(
                    "G:%s eigen:%s {%s} isGHaveMultipleEdges:%5s isEulerian:%5s\n",
                    g,
                    toEigen.get(g),
                    synchronizationEntry,
                    isGHaveMultiEdge,
                    new EulerianInspector<Integer, MyEdge>().isGraphEulerianCycle(g)
            );
            //if (synchronizationEntry.equals(new SynchronizationEntry(true, TernaryLogic.Yes)))
            //    Shower.show(g);

            /*if (!synchronizationEntry.isSynchronizable && !isGHaveMultiEdge)
                Shower.show(g);*/
            if (!synchronizationEntry.isSynchronizable && isPrintNonSyncColorings) {
                out.printf("[");
                Set<Automata<Integer, Character>> nonSyncColorings
                        = TotallySynchronizationBruteChecker.findAllNonSyncNonCharIsomorphicColorings(g);
                out.print(nonSyncColorings);
                out.printf("]\n\n");

                //Shower.show(nonSyncColoringsCache.iterator().next());
            }

        }

        //Shower.show(g);
    }
}
