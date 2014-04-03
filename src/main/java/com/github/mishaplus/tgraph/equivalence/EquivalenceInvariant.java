package com.github.mishaplus.tgraph.equivalence;

import java.util.List;

/**
 * Created by michael on 03.04.14
 */
public interface EquivalenceInvariant<T> {
    List<?> getInvariant(T elem);
}
