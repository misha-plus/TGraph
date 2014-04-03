package com.github.mishaplus.tgraph.equivalence;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by michael on 03.04.14
 */
public class DummyInvariant<T> implements EquivalenceInvariant<T> {
    @Override
    public List<?> getInvariant(T elem) {
        return Lists.newArrayList();
    }
}
