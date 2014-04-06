package com.github.mishaplus.tgraph.equivalence;

import com.github.mishaplus.tgraph.automata.Automata;
import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections4.iterators.PermutationIterator;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by michael on 06.04.14
 */
public class AutomataCharactersIsomorphism<S, A> implements EquivalenceRelation<Automata<S, A>> {
    @Override
    public boolean isEquivalent(Automata<S, A> a, Automata<S, A> b) {
        Set<S> aStates = a.getStates();
        Set<S> bStates = b.getStates();
        if (!Objects.equal(aStates, bStates))
            return false;
        @SuppressWarnings("UnnecessaryLocalVariable")
        Set<S> states = aStates;
        if (states.isEmpty())
            return true;

        S someState = states.iterator().next();

        Set<A> aAlphabet = a.getTransitions(someState).keySet();
        List<A> aOrderedAlphabet = Lists.newArrayList(aAlphabet);

        Set<A> bAlphabet = b.getTransitions(someState).keySet();
        List<A> bOrderedAlphabet = Lists.newArrayList(bAlphabet);

        if (aAlphabet.size() != bAlphabet.size())
            return false;

        Iterator<List<A>> permutations = new PermutationIterator<>(aOrderedAlphabet);
        while (permutations.hasNext()) {
            List<A> permutation = permutations.next();
            Iterator<A> permutationIterator = permutation.iterator();
            Map<A, A> possibleIsomorphism = Maps.newHashMap();
            for (int i = 0; i < permutation.size(); i++)
                possibleIsomorphism.put(permutationIterator.next(), bOrderedAlphabet.get(i));
            boolean isIsomorphism = checkCharacterIsomorphism(possibleIsomorphism, a, b);
            if (isIsomorphism)
                return true;
        }
        return false;
    }

    private boolean checkCharacterIsomorphism(
            Map<A, A> possibleIsomorphism,
            Automata<S, A> a,
            Automata<S, A> b
    ) {
        Set<S> states = a.getStates();
        for (S state : states) {
            Map<A, S> aTransitionsTransformed = Maps.newHashMap();
            for (Map.Entry<A, S> transition : a.getTransitions(state).entrySet()) {
                A ch = transition.getKey();
                S nextState = transition.getValue();

                A transformedCh = possibleIsomorphism.get(ch);
                aTransitionsTransformed.put(transformedCh, nextState);
            }
            if (!Objects.equal(aTransitionsTransformed, b.getTransitions(state)))
                return false;
        }
        return true;
    }
}
