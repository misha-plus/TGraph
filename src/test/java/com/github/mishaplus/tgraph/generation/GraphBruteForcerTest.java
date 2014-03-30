package com.github.mishaplus.tgraph.generation;

import com.github.mishaplus.tgraph.util.DirectedPseudographCreator;
import com.github.mishaplus.tgraph.util.MyEdge;
import com.github.mishaplus.tgraph.util.Util;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import org.jgrapht.graph.DirectedPseudograph;
import org.junit.Assert;
import org.junit.Test;

import java.util.Set;

public class GraphBruteForcerTest {
    @Test
    public void testBrute1vertexGraph() throws Exception {
        DirectedPseudograph<Integer, MyEdge> generationGraph = DirectedPseudographCreator.create();
        int n = 1;
        int d = 5;
        Set<DirectedPseudograph<Integer, MyEdge>> expected = Sets.newHashSet();
        for (int i = 1; i <= d; i++) {
            Util.addEdge(generationGraph, 1, 1);
            expected.add(generationGraph);
            //noinspection unchecked
            generationGraph = (DirectedPseudograph) generationGraph.clone();
        }
        Set<DirectedPseudograph<Integer, MyEdge>> actual = Sets.newHashSet();
        new GraphBruteForcer(n, d, actual::add).brute();

        Assert.assertEquals(expected, actual);
    }

    private static interface VertexVariants<V> {
        Set<Multimap<V, V>> getVariants(V first, V second);
    }

    @Test
    public void testBrute2vertexGraph() throws Exception {
        int n = 2;
        int d = 2;
        Set<DirectedPseudograph<Integer, MyEdge>> expected = Sets.newHashSet();
        VertexVariants<Integer> variants = (v1, v2) -> Sets.newHashSet(
                ImmutableMultimap.of(v1, v1),
                ImmutableMultimap.of(v1, v1, /**/ v1, v1),
                ImmutableMultimap.of(v1, v2),
                ImmutableMultimap.of(v1, v1, /**/ v1, v2),
                ImmutableMultimap.of(v1, v2, /**/ v1, v2)
        );

        decartUnite(variants.getVariants(1, 2), variants.getVariants(2, 1))
                .forEach(edges -> expected.add(DirectedPseudographCreator.create(edges)));
        Set<DirectedPseudograph<Integer, MyEdge>> actual = Sets.newHashSet();
        new GraphBruteForcer(n, d, actual::add).brute();



        Assert.assertEquals(expected, actual);
    }

    <V> Set<Multimap<V, V>> decartUnite(Set<Multimap<V, V>> first, Set<Multimap<V, V>> second) {
        Set<Multimap<V, V>> result = Sets.newHashSet();
        for (Multimap<V, V> firstMap : first) {
            for (Multimap<V, V> secondMap : second) {
                result.add(uniteMultimaps(firstMap, secondMap));
            }
        }
        return result;
    }

    <V> Multimap<V, V> uniteMultimaps(Multimap<V, V> first, Multimap<V, V> second) {
        return ImmutableMultimap.<V, V>builder().putAll(first).putAll(second).build();
    }
}
