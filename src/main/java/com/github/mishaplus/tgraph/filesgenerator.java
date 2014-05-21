package com.github.mishaplus.tgraph;

import com.github.mishaplus.tgraph.generation.GenerateSameDegreePrimitivePseudographs;
import com.github.mishaplus.tgraph.saver.GraphsSerializer;
import com.github.mishaplus.tgraph.util.MyEdge;
import org.jgrapht.graph.DirectedPseudograph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Set;

public class FilesGenerator {
    private static final Logger logger = LoggerFactory.getLogger(FilesGenerator.class);

    public static Set<DirectedPseudograph<Integer, MyEdge>> getGraphsFromFile(
            int vertexCount, int outDegree
    ) throws IOException {
        return GraphsSerializer.load(new File(getFileName(vertexCount, outDegree)));
    }

    public static void generateIfNotHave(int vertexCount, int outDegree) throws IOException {
        File file = new File(getFileName(vertexCount, outDegree));
        if (file.exists()) {
            logger.info("File {} is present. Generating is ignored", file.getAbsolutePath());
            return;
        }

        generateToFile(vertexCount, outDegree);
    }

    public static void generateToFile(int vertexCount, int outDegree) throws IOException {
        File file = new File(getFileName(vertexCount, outDegree));
        Set<DirectedPseudograph<Integer, MyEdge>> toSave
                = new GenerateSameDegreePrimitivePseudographs(vertexCount, outDegree)
                .generateAllNonIsomorphic();
        GraphsSerializer.save(file, toSave);
    }

    private static String getFileName(int vertexCount, int outDegree) {
        return String.format("graphs/graphs(%d,%d).txt", vertexCount, outDegree);
    }
}
