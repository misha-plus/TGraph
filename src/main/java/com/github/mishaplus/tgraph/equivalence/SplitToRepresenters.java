package com.github.mishaplus.tgraph.equivalence;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;

public class SplitToRepresenters<T> {
    private EquivalenceRelation<T> relation;
    private EquivalenceInvariant<T> invariant;

    public SplitToRepresenters(EquivalenceRelation<T> relation) {
        this.relation = relation;
        this.invariant = new DummyInvariant<>();
    }

    public SplitToRepresenters(
            EquivalenceRelation<T> relation,
            EquivalenceInvariant<T> equivalenceInvariant
    ) {
        this.relation = relation;
        this.invariant = equivalenceInvariant;
    }

    public Set<T> split(Set<T> input) {
        ListMultimap<List<?>, T> invariantToElements = LinkedListMultimap.create();
        for (T elem : input)
            invariantToElements.put(invariant.getInvariant(elem), elem);

        List<Integer> classesSize = Lists.newArrayList();
        //noinspection Convert2streamapi
        for (Map.Entry<List<?>, Collection<T>> invariantEntry
                : invariantToElements.asMap().entrySet())
            classesSize.add(invariantEntry.getValue().size());

        Set<T> result = Sets.newHashSet();

        for (Map.Entry<List<?>, Collection<T>> invariantEntry
                : invariantToElements.asMap().entrySet()) {
            List<T> invElements = Lists.newLinkedList(invariantEntry.getValue());
            while (!invElements.isEmpty()) {
                Iterator<T> it = invElements.iterator();
                T element = it.next();
                it.remove();
                result.add(element);

                Iterator<T> othersIt = invElements.iterator();
                while (othersIt.hasNext()) {
                    T other = othersIt.next();
                    if (relation.isEquivalent(element, other))
                        othersIt.remove();
                }
            }
        }

        return result;
    }
}
