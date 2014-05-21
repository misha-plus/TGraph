package com.github.mishaplus.tgraph;

import com.github.mishaplus.tgraph.automata.Automata;
import com.github.mishaplus.tgraph.automata.coloring.TotallySynchronizationBruteChecker;
import com.github.mishaplus.tgraph.eigen.SameOutDegreeGraphEigenvector;
import com.github.mishaplus.tgraph.numbersets.strategies.BruteForceStrategy;
import com.github.mishaplus.tgraph.numbersets.strategies.TernaryLogic;
import com.github.mishaplus.tgraph.util.DirectedPseudographCreator;
import com.github.mishaplus.tgraph.util.MyEdge;
import com.google.common.collect.*;
import org.jgrapht.graph.DirectedPseudograph;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

class SynchronizationEntry {
    final boolean isSynchronizable;
    final TernaryLogic isPartitionable;

    SynchronizationEntry(boolean isSynchronizable, TernaryLogic isPartitionable) {
        this.isSynchronizable = isSynchronizable;
        this.isPartitionable  = isPartitionable;
    }

    @Override
    public String toString() {
        return String.format("isTS:%s isPartitionable:%s", isSynchronizable, isPartitionable);
    }

    @Override
    public int hashCode() {
        return isPartitionable.hashCode()*5 + (isSynchronizable ? 1 : 0);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof SynchronizationEntry))
            return false;

        SynchronizationEntry other = (SynchronizationEntry) obj;
        return isSynchronizable == other.isSynchronizable
                && isPartitionable == other.isPartitionable;
    }
}

public class App {
    public static void main(String[] args) throws Exception {
        App app = new App();
        //app.tmp2();
        //app.tmpShow();
        //app.run();
        app.generateGraphsFiles();
    }

    public void generateGraphsFiles() throws IOException {
        for (int d : ImmutableList.of(2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12))
            FilesGenerator.generateIfNotHave(2, d);

        FilesGenerator.generateIfNotHave(3, 2);
        FilesGenerator.generateIfNotHave(3, 3);
        FilesGenerator.generateIfNotHave(3, 4);
        FilesGenerator.generateIfNotHave(3, 5);

        FilesGenerator.generateIfNotHave(4, 2);
        FilesGenerator.generateIfNotHave(4, 3);

        FilesGenerator.generateIfNotHave(5, 2);
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

    public void run() throws Exception {
        Set<DirectedPseudograph<Integer, MyEdge>> generated = FilesGenerator.getGraphsFromFile(3, 2);

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

            SynchronizationEntry syncEntry = new SynchronizationEntry(
                    isTotallySynchronizable,
                    isCanBePartitioned
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

            System.out.printf(
                    "G:%s eigen:%s {%s} isGHaveMultipleEdges:%s isEulerian:%s\n",
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
            if (!synchronizationEntry.isSynchronizable) {
                System.out.printf("[");
                Set<Automata<Integer, Character>> nonSyncColorings
                        = TotallySynchronizationBruteChecker.findAllNonSyncNonCharIsomorphicColorings(g);
                System.out.print(nonSyncColorings);
                System.out.printf("]\n\n");

                //Shower.show(nonSyncColorings.iterator().next());
            }

        }

        //Shower.show(g);
    }
}
