package com.github.mishaplus.tgraph;

import static org.junit.Assert.*;

import com.github.mishaplus.tgraph.generation.GenerateSameDegreePrimitivePseudographs;
import com.github.mishaplus.tgraph.saver.GraphsSerializer;
import org.junit.Test;

import java.io.File;

public class FilesGeneratorTest {
    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Test
    public void testGetGraphsFromFile() throws Exception {
        new File("graphs/graphs(3,2).txt").delete();
        FilesGenerator.generateToFile(3, 2);
        assertEquals(
                new GenerateSameDegreePrimitivePseudographs(3, 2).generateAllNonIsomorphic(),
                FilesGenerator.getGraphsFromFile(3, 2)
        );
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Test
    public void testGenerateToFile() throws Exception {
        new File("graphs/graphs(3,2).txt").delete();
        FilesGenerator.generateToFile(3, 2);
        assertEquals(
                new GenerateSameDegreePrimitivePseudographs(3, 2).generateAllNonIsomorphic(),
                GraphsSerializer.load(new File("graphs/graphs(3,2).txt"))
        );
    }
}
