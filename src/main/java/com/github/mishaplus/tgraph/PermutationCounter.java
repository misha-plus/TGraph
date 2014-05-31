package com.github.mishaplus.tgraph;

import com.github.mishaplus.tgraph.permanent.PermanentEvaluator;
import com.github.mishaplus.tgraph.util.MyEdge;
import org.jgrapht.graph.DirectedPseudograph;

public class PermutationCounter {
    private PermanentEvaluator permanentEvaluator = new PermanentEvaluator();

    public int count(DirectedPseudograph<Integer, MyEdge> graph) {
        IntegerMatrix adjMatrix = Converter.toIntAdjArray(graph);
        replaceNonZerosToOnes(adjMatrix);
        return permanentEvaluator.evaluate(adjMatrix);
    }

    public void replaceNonZerosToOnes(IntegerMatrix matrix) {
        for (int i = 0; i < matrix.n; i++)
            for (int j = 0; j < matrix.m; j++)
                matrix.matrix[i][j] = matrix.matrix[i][j] == 0 ? 0 : 1;
    }
}
