package com.github.mishaplus.tgraph.equivalence;

public interface EquivalenceRelation<T> {
    boolean isEquivalent(T a, T b);
}
