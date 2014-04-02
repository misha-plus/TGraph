package com.github.mishaplus.tgraph.automata.coloring;

import com.github.mishaplus.tgraph.automata.Automata;
import com.github.mishaplus.tgraph.automata.AutomataImpl;
import com.github.mishaplus.tgraph.util.DirectedPseudographCreator;
import com.github.mishaplus.tgraph.util.MyEdge;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multimap;
import org.jgrapht.graph.DirectedPseudograph;
import org.junit.Assert;
import org.junit.Test;

import java.util.Map;
import java.util.Set;

/**
 * Created by michael on 02.04.14
 */
public class BruteAllColoringsTest {
    @Test
    public void testBrute() throws Exception {
        Multimap<Integer, Integer> edges = ImmutableMultimap.<Integer, Integer>builder()
                .put(1, 2).put(1, 3)
                .put(2, 1).put(2, 1)
                .put(3, 2).put(3, 3)
                .build();

        DirectedPseudograph<Integer, MyEdge> g = DirectedPseudographCreator.create(edges);

        Automata<Integer, Character> firstColoring = generateBaseColoring();
        firstColoring.addTransition(3, 2, 'a');
        firstColoring.addTransition(3, 3, 'b');

        Automata<Integer, Character> secondColoring = generateBaseColoring();
        secondColoring.addTransition(3, 2, 'b');
        secondColoring.addTransition(3, 3, 'a');

        Set<Automata<Integer, Character>> allColorings = ImmutableSet.of(
                firstColoring,
                secondColoring,
                swapChars(firstColoring),
                swapChars(secondColoring)
        );

        Assert.assertEquals(allColorings, new BruteAllColorings(g).brute());
    }

    private Automata<Integer, Character> generateBaseColoring() {
        Automata<Integer, Character> baseColoring = new AutomataImpl<>();
        baseColoring.addTransition(1, 2, 'a');
        baseColoring.addTransition(1, 3, 'b');

        baseColoring.addTransition(2, 1, 'a');
        baseColoring.addTransition(2, 1, 'b');
        return baseColoring;
    }

    private Automata<Integer, Character> swapChars(Automata<Integer, Character> automata) {
        Automata<Integer, Character> result = new AutomataImpl<>();

        Map<Character, Character> swapping = ImmutableMap.of(
                'a', 'b',
                'b', 'a'
        );

        for (Integer from: automata.getStates()) {
            for (Map.Entry<Character, Integer> transition: automata.getTransitions(from).entrySet()) {
                Character ch = transition.getKey();
                Integer to = transition.getValue();
                result.addTransition(from, to, swapping.get(ch));
            }
        }
        return result;
    }
}
