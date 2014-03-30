package com.github.mishaplus.tgraph.equivalence;

import com.google.common.collect.Sets;

import java.util.Iterator;
import java.util.Set;

public class SplitToRepresenters<T> {
    private EquivalenceRelation<T> relation;

    public SplitToRepresenters(EquivalenceRelation<T> relation) {
        this.relation = relation;
    }

    public Set<T> split(Set<T> input) {
        Set<T> elements = Sets.newHashSet(input);
        Set<T> result = Sets.newHashSet();

        while (!elements.isEmpty()) {
            Iterator<T> it = elements.iterator();
            T element = it.next();
            it.remove();
            result.add(element);

            Iterator<T> othersIt = elements.iterator();
            while (othersIt.hasNext()) {
                T other = othersIt.next();
                if (relation.isEquivalent(element, other))
                    othersIt.remove();
            }
        }

        return result;
    }
}
