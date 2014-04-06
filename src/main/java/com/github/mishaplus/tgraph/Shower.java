package com.github.mishaplus.tgraph;

import com.github.mishaplus.tgraph.automata.Automata;
import com.github.mishaplus.tgraph.util.Util;
import com.google.common.collect.Maps;
import com.google.common.collect.MultimapBuilder;
import com.google.common.collect.SetMultimap;
import com.google.common.collect.Sets;
import org.jgraph.JGraph;
import org.jgrapht.Graph;
import org.jgrapht.ext.JGraphModelAdapter;
import org.jgrapht.graph.DirectedPseudograph;

import javax.swing.*;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Created by michael on 01.04.14
 */
public class Shower {
    public static <V, E> void show(Graph<V, E> graph) throws InterruptedException {
        JFrame frame = new JFrame();
        frame.setSize(400, 400);
        JGraph jgraph = new JGraph(new JGraphModelAdapter<>(graph));
        frame.getContentPane().add(jgraph);
        frame.setVisible(true);
        while (true) {
            if (!frame.isVisible())
                break;
            Thread.sleep(500);
        }
    }

    public static <S, A> void show(Automata<S, A> automata) throws InterruptedException {
        class AutomataEdge {
            S from, to;
            Set<A> transitionChars;

            AutomataEdge(S from, S to, Set<A> chars) {
                this.from = from;
                this.to   = to;
                this.transitionChars = chars;
            }

            @Override
            public int hashCode() {
                return Objects.hash(from, to, transitionChars);
            }

            @Override
            public boolean equals(Object obj) {
                if (!(obj instanceof AutomataEdge))
                   return false;
                AutomataEdge other = (AutomataEdge) obj;
                return from.equals(other.from) && to.equals(other.to)
                        && transitionChars.equals(other.transitionChars);
            }

            @Override
            public String toString() {
                return transitionChars.toString();
            }
        }

        Map<S, SetMultimap<S, A>> baseEdges = Maps.newHashMap();

        for (Map.Entry<S, Map<A, S>> stateTransitions : automata.getTransitions().entrySet()) {
            S from = stateTransitions.getKey();
            Map<A, S> fromTransitions = stateTransitions.getValue();
            for (Map.Entry<A, S> fromTransition : fromTransitions.entrySet()) {
                S to = fromTransition.getValue();
                A ch = fromTransition.getKey();

                putEdge(baseEdges, from, to, ch);
            }
        }

        DirectedPseudograph<S, AutomataEdge> baseGraph
            = new DirectedPseudograph<>(AutomataEdge.class);

        Util.addVerticesIfNotContains(baseGraph, automata.getStates());
        for (Map.Entry<S, SetMultimap<S, A>> stateToEdges : baseEdges.entrySet()) {
            S from = stateToEdges.getKey();
            SetMultimap<S, A> edges = stateToEdges.getValue();
            for (Map.Entry<S, Collection<A>> edge : edges.asMap().entrySet()) {
                S to = edge.getKey();
                Set<A> chars = Sets.newHashSet(edge.getValue());
                AutomataEdge automataEdge = new AutomataEdge(from, to, chars);

                baseGraph.addEdge(from, to, automataEdge);
            }
        }

        show(baseGraph);
    }

    private static <S, A> void putEdge(Map<S, SetMultimap<S, A>> baseEdges, S from, S to, A ch) {
        if (!baseEdges.containsKey(from))
            baseEdges.put(from, MultimapBuilder.hashKeys().hashSetValues().build());
        baseEdges.get(from).put(to, ch);
    }
}
