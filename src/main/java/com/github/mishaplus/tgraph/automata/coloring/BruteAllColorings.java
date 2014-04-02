package com.github.mishaplus.tgraph.automata.coloring;

import com.github.mishaplus.tgraph.automata.Automata;
import com.github.mishaplus.tgraph.automata.AutomataImpl;
import com.github.mishaplus.tgraph.util.MyEdge;
import com.github.mishaplus.tgraph.util.Util;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.commons.collections4.iterators.PermutationIterator;
import org.jgrapht.graph.DirectedPseudograph;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

/**
 * Created by michael on 02.04.14
 */
public class BruteAllColorings {
    private final DirectedPseudograph<Integer, MyEdge> g;
    private final Map<Integer, Integer> numToVertex; // 0, ... ,n-1
    private final List<Character> alphabet;
    private final int n;
    private final int degree;

    public BruteAllColorings(DirectedPseudograph<Integer, MyEdge> g) {
        Preconditions.checkNotNull(g);
        Preconditions.checkArgument(g.vertexSet().size() > 0);
        int someOutDegree = g.outDegreeOf(g.vertexSet().iterator().next());
        for (int v: g.vertexSet())
            Preconditions.checkArgument(g.outDegreeOf(v) == someOutDegree);

        numToVertex = Maps.newHashMap();
        {
            int idx = 0;
            for (int v: g.vertexSet()) {
                numToVertex.put(idx, v);
                idx++;
            }
        }

        this.g = g;
        n = g.vertexSet().size();
        degree = someOutDegree;

        alphabet = generateAlphabet(someOutDegree);
    }

    public Set<Automata<Integer, Character>> brute() {
        Set<Automata<Integer, Character>> result = Sets.newHashSet();
        colorVertex(0, new AutomataImpl<>(), result::add);
        return result;
    }

    private void colorVertex(
            int depth,
            Automata<Integer, Character> automata,
            Consumer<Automata<Integer, Character>> action
    ) {
        if (depth == n) {
            action.accept(automata);
            return;
        }
        int v = numToVertex.get(depth);
        List<Integer> outGoing = Lists.newArrayList();
        for (MyEdge edge: g.outgoingEdgesOf(v))
            outGoing.add(g.getEdgeTarget(edge));

        Iterator<List<Integer>> permutationIterator
                = new PermutationIterator<>(Util.generateList(0, degree-1));
        while (permutationIterator.hasNext()) {
            List<Integer> permutation = permutationIterator.next();
            Automata<Integer, Character> nextAutomata = automata.copy();
            {
                int idx = 0;
                for (int permIdx : permutation) {
                    nextAutomata.addTransition(v, outGoing.get(idx), alphabet.get(permIdx));
                    idx++;
                }
            }
            colorVertex(depth+1, nextAutomata, action);
        }
    }

    private List<Character> generateAlphabet(int degree) {
        Preconditions.checkArgument(degree < 'z' - 'a' + 1);
        List<Character> result = Lists.newArrayList();
        for (char ch = 'a'; ch <= 'a' + degree; ch++)
            result.add(ch);
        return result;
    }
}
