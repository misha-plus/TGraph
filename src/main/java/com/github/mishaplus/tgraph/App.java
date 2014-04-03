package com.github.mishaplus.tgraph;

import com.github.mishaplus.tgraph.eigen.SameOutDegreeGraphEigenvector;
import com.github.mishaplus.tgraph.generation.GenerateSameDegreePrimitivePseudographs;
import com.github.mishaplus.tgraph.numbersets.strategies.BruteForceStrategy;
import com.github.mishaplus.tgraph.numbersets.strategies.TernaryLogic;
import com.github.mishaplus.tgraph.util.MyEdge;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableMultiset;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import org.jgrapht.graph.DirectedPseudograph;

import java.util.*;

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
        new App().run();
    }

    public void run() throws Exception {
        Set<DirectedPseudograph<Integer, MyEdge>> generated
                = new GenerateSameDegreePrimitivePseudographs(5, 2).generateAllNonIsomorphic();

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
            System.out.printf(
                    "G:%s eigen:%s {%s}\n",
                    g,
                    toEigen.get(g),
                    synchronizationEntry
            );
            //if (synchronizationEntry.equals(new SynchronizationEntry(true, TernaryLogic.Yes)))
            //    Shower.show(g);
        }

        //Shower.show(g);
    }
}
