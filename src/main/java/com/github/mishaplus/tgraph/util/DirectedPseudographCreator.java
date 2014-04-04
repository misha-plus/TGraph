package com.github.mishaplus.tgraph.util;

import com.github.mishaplus.tgraph.IntegerMatrix;
import com.google.common.base.Preconditions;
import com.google.common.collect.*;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.jgrapht.graph.DirectedPseudograph;

import java.util.Arrays;
import java.util.Set;

public class DirectedPseudographCreator {
    public static DirectedPseudograph<Integer, MyEdge> create() {
        return new DirectedPseudograph<>(MyEdge.class);
    }

    public static DirectedPseudograph<Integer, MyEdge> create(
            Multimap<Integer, Integer> edges
    ) {
        DirectedPseudograph<Integer, MyEdge> result = DirectedPseudographCreator.create();
        //noinspection RedundantTypeArguments
        edges.entries().forEach(edge ->
                Util.<Integer, MyEdge>addEdge(result, edge.getKey(), edge.getValue())
        );
        return result;
    }

    public static DirectedPseudograph<Integer, MyEdge> create(
            IntegerMatrix adjacencyMatrix
    ) {
        Preconditions.checkArgument(adjacencyMatrix.n == adjacencyMatrix.m);
        Multimap<Integer, Integer> edges = ArrayListMultimap.create();
        for (int i = 0; i < adjacencyMatrix.n; i++)
            for (int j = 0; j < adjacencyMatrix.m; j++)
                for (int k = 0; k < adjacencyMatrix.matrix[i][j]; k++)
                    edges.put(i+1, j+1);
        return create(edges);
    }

    /**
     * format is (all spaces is reducible):
     * S -> [^(]* ( \[ Is \], \[ Es \] ) .*
     * Is -> I | Is, I
     * I is integer
     * Es -> Es, E
     * E -> ( NB ) = (I, I)
     *
     * format sample "G:([1, 2, 3, 4], [
     * (1->2^1)=(1,2), (1->3^1)=(1,3),
     * (2->4^1)=(2,4), (2->4^2)=(2,4),
     * (3->4^1)=(3,4), (3->4^2)=(3,4),
     * (4->1^1)=(4,1), (4->4^1)=(4,4)
     * ]) ..."
     */
    public static DirectedPseudograph<Integer, MyEdge> fromString(String s) {
        Set<String> spaces = ImmutableSet.of(" ", "\n", "\r", "\t");
        for (String space : spaces)
            s = s.replace(space, "");

        int gStartIdx = s.indexOf('(');

        String graphAndOthers = s.substring(gStartIdx);

        int verticesStringStart = graphAndOthers.indexOf('[') + 1;
        int verticesStringEnd   = graphAndOthers.indexOf(']');
        String verticesString = graphAndOthers.substring(
                verticesStringStart,
                verticesStringEnd
        );

        Set<Integer> vertices = parseVertices(verticesString);

        String edgesAndOthers = graphAndOthers.substring(verticesStringEnd + 2); // ],[...

        int edgesStringStart = edgesAndOthers.indexOf('[');
        int edgesStringEnd   = edgesAndOthers.indexOf(']');
        String edgesString = edgesAndOthers.substring(edgesStringStart, edgesStringEnd);

        Multimap<Integer, Integer> edges = parseEdges(edgesString);

        DirectedPseudograph<Integer, MyEdge> result = create(edges);
        Util.addVerticesIfNotContains(result, Lists.newArrayList(vertices));
        return result;
    }

    private static Set<Integer> parseVertices(String verticesString) {
        return Sets.newHashSet(
                Arrays.stream(verticesString.split(",")).map(Integer::valueOf).iterator()
        );
    }

    private static Multimap<Integer, Integer> parseEdges(String edgesString) {
        edgesString = "Q" + edgesString.replace("[", "").replace("]", "");

        Multimap<Integer, Integer> edges = ArrayListMultimap.create();
        // Q(...)=(x1,y1),(...)=(x2,y2),...,(...)=(xn,yn)
        String[] splitted = edgesString.split("[()]"); //{"Q"}{"..."}{"="}{"x1,y1"}{","}...{","}{"..."}{"="}{"xn,yn"}
        for (int i = 3; i < splitted.length; i += 4) {
            Pair<Integer, Integer> edge = parseEdge(splitted[i]);
            edges.put(edge.getLeft(), edge.getRight());
        }
        return edges;
    }

    private static Pair<Integer, Integer> parseEdge(String edge) {
        String[] edgeComponents = edge.split(",");
        return ImmutablePair.of(
                Integer.valueOf(edgeComponents[0]),
                Integer.valueOf(edgeComponents[1])
        );
    }
}
