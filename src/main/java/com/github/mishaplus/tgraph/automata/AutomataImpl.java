package com.github.mishaplus.tgraph.automata;

import com.github.mishaplus.tgraph.automata.synchronization.SqAutomataState;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.*;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;

/**
 * Created by michael on 01.04.14
 */
public class AutomataImpl<S, A> implements Automata<S, A> {
    private Map<S, Map<A, S>> transitions;

    public AutomataImpl() {
        transitions = Maps.newHashMap();
    }

    @Override
    public void addState(S state) {
        if (!transitions.containsKey(state))
            transitions.put(state, Maps.newHashMap());
    }

    @Override
    public void addTransition(S from, S to, A ch) {
        addState(from);
        addState(to);
        Map<A, S> outGoingOfFrom = transitions.get(from);
        if (outGoingOfFrom.containsKey(ch) && !outGoingOfFrom.get(ch).equals(to))
            throw new IllegalStateException(String.format(
                    "Automata already contains transition with same state and char"
            ));
        outGoingOfFrom.put(ch, to);
    }

    @Override
    public Set<S> getStates() {
        return Collections.unmodifiableSet(transitions.keySet());
    }

    @Override
    public Map<A, S> getOutGoing(S from) {
        return Collections.unmodifiableMap(transitions.get(from));
    }

    @Override
    public boolean isSynchronizationWord(List<A> word) {
        Set<S> states = transitions.keySet();
        for (A ch: word) {
            Set<S> nextStates = Sets.newHashSet();
            //noinspection Convert2streamapi
            for (S state: states)
                nextStates.add(makeTransition(state, ch));
            states = nextStates;
        }
        return states.size() == 1;
    }

    private void assertStateExists(S state) {
        if (!transitions.containsKey(state))
            throw new IllegalStateException(String.format(
                    "Automata not contains '%s' state", state
            ));
    }

    private void assertTransitionExists(S state, A ch) {
        Map<A, S> stateTransitions = transitions.get(state);
        if (!stateTransitions.containsKey(ch))
            throw new IllegalStateException(String.format(
                    "Automata state '%s' not contains '%s' transition", state, ch
            ));
    }

    @Override
    public List<A> findSynchronizationWord() {
        Automata<SqAutomataState<S>, A> pairAutomata = toPairAutomata();
        SqAutomataState<S> q0 = SqAutomataState.createCollapsingState();
        Set<SqAutomataState<S>> Q = Sets.newHashSet(pairAutomata.getStates());
        List<A> word = Lists.newArrayList();
        Set<SqAutomataState<S>> q0Set = ImmutableSet.of(q0);

        while (!Q.equals(q0Set)) {
            Set<SqAutomataState<S>> QWithoutQ0 = Sets.difference(Q, q0Set);
            List<A> greedyWord = pairAutomata.findShortestPath(QWithoutQ0, q0);
            if (greedyWord == null)
                return null;
            Q = pairAutomata.makeTransition(Q, greedyWord);
            word.addAll(greedyWord);
        }

        return word;
    }

    @Override
    public S makeTransition(S state, A ch) {
        assertStateExists(state);
        assertTransitionExists(state, ch);
        return transitions.get(state).get(ch);
    }

    @Override
    public Set<S> makeTransition(Set<S> from, A ch) {
        Set<S> result = Sets.newHashSet();
        //noinspection Convert2streamapi
        for (S state: from)
            result.add(makeTransition(state, ch));
        return result;
    }

    @Override
    public S makeTransition(S state, List<A> word) {
        S q = state;
        for (A ch: word)
            q = makeTransition(q, ch);
        return q;
    }

    @Override
    public Set<S> makeTransition(Set<S> from, List<A> word) {
        Set<S> result = from;
        for (A ch: word)
            result = makeTransition(result, ch);
        return result;
    }

    @Override
    public Map<S, Map<A, S>> getTransitions() {
        return Collections.unmodifiableMap(transitions);
    }

    @Override
    public Map<A, S> getTransitions(S state) {
        return Collections.unmodifiableMap(transitions.get(state));
    }

    private void assertCorrectColoring() {
        Preconditions.checkState(!transitions.isEmpty(), "Automata not contains states");
        Set<A> alphabet = getSomeStateAlphabet();
        for (Map.Entry<S, Map<A, S>> stateTransitions: transitions.entrySet())
            Preconditions.checkState(Objects.equal(
                    stateTransitions.getValue().keySet(),
                    alphabet
            ), "Automata outgoing chars set is different for some states");
    }

    private Set<A> getSomeStateAlphabet() {
        return transitions.entrySet().iterator().next().getValue().keySet();
    }

    @Override
    public Automata<SqAutomataState<S>, A> toPairAutomata() {
        assertCorrectColoring();
        Automata<SqAutomataState<S>, A> pairAutomata = new AutomataImpl<>();
        Set<A> alphabet = getSomeStateAlphabet();

        SqAutomataState<S> q0 = SqAutomataState.createCollapsingState();
        for (A ch: alphabet)
            pairAutomata.addTransition(q0, q0, ch);

        for (S p: transitions.keySet())
            for (S q: transitions.keySet()) {
                if (p.equals(q))
                    continue;
                for (A ch : alphabet) {
                    S pCh = makeTransition(p, ch);
                    S qCh = makeTransition(q, ch);

                    if (Objects.equal(pCh, qCh))
                        pairAutomata.addTransition(
                                new SqAutomataState<>(p, q),
                                q0,
                                ch
                        );
                    else
                        pairAutomata.addTransition(
                                new SqAutomataState<>(p, q),
                                new SqAutomataState<>(pCh, qCh),
                                ch
                        );
                }
            }
        return pairAutomata;
    }

    @Override
    public List<A> findShortestPath(Set<S> from, S to) {
        Queue<S> Q = Queues.newArrayDeque(from);
        Map<S, Pair<S, A>> parents = Maps.newHashMap();
        for (S state: from)
            parents.put(state, null);

        while (true) {
            if (Q.isEmpty())
                break;
            S q = Q.remove();
            if (q.equals(to))
                break;
            for (Map.Entry<A, S> transition: getTransitions(q).entrySet()) {
                S next = transition.getValue();
                A ch   = transition.getKey();
                if (!parents.containsKey(next)) {
                    Q.add(next);
                    parents.put(next, Pair.of(q, ch));
                }
            }
        }

        if (!parents.containsKey(to))
            return null;

        List<A> result = Lists.newArrayList();
        S q = to;
        while (parents.get(q) != null) {
            Pair<S, A> transition = parents.get(q);
            result.add(transition.getRight());
            q = transition.getLeft();
        }
        return Lists.reverse(result);
    }



    @Override
    public int hashCode() {
        return transitions.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        //noinspection SimplifiableIfStatement
        if (!(obj instanceof Automata))
            return false;
        return transitions.equals(((Automata) obj).getTransitions());
    }

    @Override
    public String toString() {
        return Objects.toStringHelper("Automata")
                .add("Transitions", transitions)
                .toString();
    }
}
