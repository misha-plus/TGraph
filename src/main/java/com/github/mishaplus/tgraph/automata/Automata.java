package com.github.mishaplus.tgraph.automata;

import com.github.mishaplus.tgraph.automata.synchronization.SqAutomataState;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by michael on 01.04.14
 */
public interface Automata<S, A> {
    void addState(S state);
    void addTransition(S from, S to, A ch);

    Set<S> getStates();
    Map<A, S> getOutGoing(S from);

    boolean isSynchronizationWord(List<A> word);
    List<A> findSynchronizationWord();

    S makeTransition(S state, A ch);
    Set<S> makeTransition(Set<S> from, A ch);

    S makeTransition(S state, List<A> word);
    Set<S> makeTransition(Set<S> from, List<A> word);

    Map<S, Map<A, S>> getTransitions();
    Map<A, S> getTransitions(S state);

    Automata<SqAutomataState<S>, A> toPairAutomata();

    List<A> findShortestPath(Set<S> from, S to);
}
