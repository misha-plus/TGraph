package com.github.mishaplus.tgraph;

import com.github.mishaplus.tgraph.eigen.SameOutDegreeGraphEigenvector;
import com.github.mishaplus.tgraph.generation.GenerateSameDegreeAgwPseudographs;
import com.github.mishaplus.tgraph.util.MyEdge;
import org.jgrapht.graph.DirectedPseudograph;

import java.util.Set;

public class App {
    public static void main(String[] args) throws Exception {
        new App().run();
    }

    public void run() throws Exception {
        Set<DirectedPseudograph<Integer, MyEdge>> generated3verticesAnd2OutDegree
                = new GenerateSameDegreeAgwPseudographs(3, 2).generateAllNonIsomorphic();
        for (DirectedPseudograph<Integer, MyEdge> g: generated3verticesAnd2OutDegree) {
            System.out.print(g + " ");
            System.out.println(
                    new SameOutDegreeGraphEigenvector()
                            .getFriedmanEigenvectorWithRelativelyPrimeComponents(g)
            );
        }
    }
}
