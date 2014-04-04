package com.github.mishaplus.tgraph.eigen;

import com.github.mishaplus.tgraph.IntegerMatrix;

import java.util.List;

/**
 * Created by michael on 04.04.14
 */
public interface EigenvectorFinder {
    List<Integer> findPositiveEigenvector(IntegerMatrix matrix, int eigenvalue);
}
