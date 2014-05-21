package com.github.mishaplus.tgraph.diophantineequation;

import com.github.mishaplus.tgraph.IntegerMatrix;
import com.github.mishaplus.tgraph.util.MyWeightedEdge;
import com.google.common.base.Preconditions;
import org.jgrapht.alg.EdmondsKarpMaximumFlow;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;

public class FixedDegreeEulerianFinder {
    private final IntegerMatrix edges;
    private final int vertexCount;
    private final int toDegree;

    public FixedDegreeEulerianFinder(
            IntegerMatrix edges, int toDegree
    ) {
        Preconditions.checkArgument(edges.m == edges.n);
        for (int i = 0; i < edges.n; i++)
            for (int j = 0; j < edges.m; j++)
                Preconditions.checkArgument(edges.matrix[i][j] >= 0);
        this.edges = edges;
        this.toDegree = toDegree;
        this.vertexCount = edges.n;
    }

    public DefaultDirectedWeightedGraph<Integer, MyWeightedEdge> createFlowGraph() {
        DefaultDirectedWeightedGraph<Integer, MyWeightedEdge> flowGraph
                = new DefaultDirectedWeightedGraph<>(MyWeightedEdge.class);
        flowGraph.addVertex(getSourceVertex());
        flowGraph.addVertex(getTargetVertex());
        for (int i = 0; i < vertexCount; i++)
            flowGraph.addVertex(getLeftVertex(i));
        for (int i = 0; i < vertexCount; i++)
            flowGraph.addVertex(getRightVertex(i));

        for (int i = 0; i < vertexCount; i++)
            for (int j = 0; j < vertexCount; j++) {
                if (edges.matrix[i][j] != 0) {
                    // toDegree as infinity weight
                    int weight = toDegree;
                    MyWeightedEdge edge = new MyWeightedEdge(getLeftVertex(i), getRightVertex(j), weight);

                    flowGraph.addEdge(getLeftVertex(i), getRightVertex(j), edge);
                    flowGraph.setEdgeWeight(edge, weight);
                }
            }

        for (int i = 0; i < vertexCount; i++) {
            int weight = toDegree - getOutputSum(i);
            MyWeightedEdge edge = new MyWeightedEdge(getSourceVertex(), getLeftVertex(i), weight);

            flowGraph.addEdge(getSourceVertex(), getLeftVertex(i), edge);
            flowGraph.setEdgeWeight(edge, weight);
        }

        for (int i = 0; i < vertexCount; i++) {
            int weight = toDegree - getInputSum(i);
            MyWeightedEdge edge = new MyWeightedEdge(getRightVertex(i), getTargetVertex(), weight);

            flowGraph.addEdge(getRightVertex(i), getTargetVertex(), edge);
            flowGraph.setEdgeWeight(edge, weight);
        }

        return flowGraph;
    }

    public boolean isSolveable() {
        DefaultDirectedWeightedGraph<Integer, MyWeightedEdge> flowGraph = createFlowGraph();
        for (MyWeightedEdge edge : flowGraph.edgeSet())
            if (edge.weight < 0)
                return false;
        EdmondsKarpMaximumFlow<Integer, MyWeightedEdge> maxFlow = new EdmondsKarpMaximumFlow<>(flowGraph);
        maxFlow.calculateMaximumFlow(getSourceVertex(), getTargetVertex());
        int expectedInputFlow = 0;
        for (int i = 0; i < vertexCount; i++)
            expectedInputFlow += toDegree - getInputSum(i);
        int expectedOutputFlow = 0;
        for (int i = 0; i < vertexCount; i++)
            expectedOutputFlow += toDegree - getOutputSum(i);



        return expectedInputFlow == expectedOutputFlow &&
                Math.abs(maxFlow.getMaximumFlowValue() - expectedInputFlow) <= EdmondsKarpMaximumFlow.DEFAULT_EPSILON;
    }

    private int getOutputSum(int fromVertex) {
        int result = 0;
        for (int toVertex = 0; toVertex < vertexCount; toVertex++)
            result += edges.matrix[fromVertex][toVertex];
        return result;
    }

    private int getInputSum(int toVertex) {
        int result = 0;
        for (int fromVertex = 0; fromVertex < vertexCount; fromVertex++)
            result += edges.matrix[fromVertex][toVertex];
        return result;
    }

    private int getLeftVertex(int vertex) {
        return vertex;
    }

    private int getRightVertex(int vertex) {
        return vertexCount + vertex;
    }

    private int getSourceVertex() {
        //noinspection PointlessArithmeticExpression
        return 2*vertexCount + 0;
    }

    private int getTargetVertex() {
        return 2*vertexCount + 1;
    }
}
