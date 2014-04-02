package com.github.mishaplus.tgraph.automata.synchronization;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableSet;

/**
 * Created by michael on 02.04.14
 */
public class SqAutomataState<T> {
    private final ImmutableSet<T> set;

    public SqAutomataState(T elem) {
        set = ImmutableSet.of(elem);
    }

    public SqAutomataState(T a, T b) {
        set = ImmutableSet.of(a, b);
    }

    public SqAutomataState() {
        set = null;
    }

    public static <T> SqAutomataState<T> createCollapsingState() {
        return new SqAutomataState<>();
    }

    @Override
    public boolean equals(Object obj) {
        //noinspection SimplifiableIfStatement
        if (obj instanceof SqAutomataState)
            return Objects.equal(set, ((SqAutomataState) obj).set);
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(set);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper("SqAutomataState")
                .addValue(set)
                .toString();
    }
}
