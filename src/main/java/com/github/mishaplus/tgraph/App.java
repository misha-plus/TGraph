package com.github.mishaplus.tgraph;

import com.github.mishaplus.tgraph.eigen.SameOutDegreeGraphEigenvector;
import com.github.mishaplus.tgraph.generation.GenerateSameDegreeAgwPseudographs;
import com.github.mishaplus.tgraph.numbersets.strategies.BruteForceStrategy;
import com.github.mishaplus.tgraph.numbersets.strategies.TernaryLogic;
import com.github.mishaplus.tgraph.util.MyEdge;
import com.google.common.collect.ImmutableMultiset;
import org.jgrapht.graph.DirectedPseudograph;

import java.util.List;
import java.util.Set;

public class App {
    public static void main(String[] args) throws Exception {
        new App().run();
    }

    public void run() throws Exception {
        Set<DirectedPseudograph<Integer, MyEdge>> generated3verticesAnd2OutDegree
                = new GenerateSameDegreeAgwPseudographs(4, 2).generateAllNonIsomorphic();
        for (DirectedPseudograph<Integer, MyEdge> g: generated3verticesAnd2OutDegree) {
            List<Integer> eigenvector = new SameOutDegreeGraphEigenvector()
                    .getFriedmanEigenvectorWithRelativelyPrimeComponents(g);

            TernaryLogic isCanBePartitioned = new BruteForceStrategy()
                    .isCanBePartedToSameSumSets(ImmutableMultiset.copyOf(eigenvector));

            System.out.printf(
                    "G:%s eigen:%s isCanBePartitioned:%s\n",
                    g,
                    eigenvector,
                    isCanBePartitioned
            );
            //Shower.show(g);
        }
    }
}
