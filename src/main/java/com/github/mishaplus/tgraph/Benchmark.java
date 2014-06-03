package com.github.mishaplus.tgraph;

import com.github.mishaplus.tgraph.generation.SameDegreeGraphBruteForcer;
import com.github.mishaplus.tgraph.util.MyEdge;
import com.google.common.collect.Sets;
import org.jgrapht.graph.DirectedPseudograph;

import java.util.Set;

public class Benchmark {
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        int[] cnt = new int[1];
        Set<DirectedPseudograph<Integer, MyEdge>> generatedSameDegree = Sets.newHashSet();
        new SameDegreeGraphBruteForcer(
                2,
                2,
                g -> {
                    Set<DirectedPseudograph<Integer, MyEdge>> s = generatedSameDegree;
                    //if (PrimitiveGraphInspector.isPrimitive(g))
                        generatedSameDegree.add(g);
                    int a = cnt[0];
                    cnt[0]++;
                }
        ).brute();

        Set<DirectedPseudograph<Integer, MyEdge>> nonIsomorphic = Sets.newHashSet();
                //= new SplitToRepresenters<>(new GraphIsomorphismRelation(), new DummyInvariant<>())
                //.split(generatedSameDegree);

        long endTime = System.currentTimeMillis();
        System.out.printf("Time to generation %s, size %s, isomorphism classes %s", (endTime - startTime), cnt[0], nonIsomorphic.size());
    }
}
