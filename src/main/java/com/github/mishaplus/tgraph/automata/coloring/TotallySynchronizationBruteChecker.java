package com.github.mishaplus.tgraph.automata.coloring;

import com.github.mishaplus.tgraph.automata.Automata;
import com.github.mishaplus.tgraph.util.MyEdge;
import com.google.common.collect.Sets;
import org.jgrapht.graph.DirectedPseudograph;

import java.util.Set;

/**
 * Created by michael on 02.04.14
 */
public class TotallySynchronizationBruteChecker {
    public static boolean checkTS(DirectedPseudograph<Integer, MyEdge> g) {
        Set<Automata<Integer, Character>> colorings = new BruteAllColorings(g).brute();
        for (Automata<Integer, Character> coloring : colorings)
            if (coloring.findSynchronizationWord() == null)
                return false;
        return true;
    }

    public static Set<Automata<Integer, Character>> findNonSyncColorings(
            DirectedPseudograph<Integer, MyEdge> g
    ) {
        Set<Automata<Integer, Character>> colorings = new BruteAllColorings(g).brute();
        Set<Automata<Integer, Character>> nonSyncColorings = Sets.newHashSet();

        colorings.stream()
                .filter(automata -> automata.findSynchronizationWord() == null)
                .forEach(nonSyncColorings::add);
        return nonSyncColorings;
    }
}
