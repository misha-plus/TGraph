package com.github.mishaplus.tgraph.saver;

import static org.junit.Assert.*;

import com.github.mishaplus.tgraph.util.DirectedPseudographCreator;
import com.github.mishaplus.tgraph.util.MyEdge;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Sets;
import org.jgrapht.graph.DirectedPseudograph;
import org.junit.Test;

import java.io.File;
import java.util.Set;

public class GraphListSerializerTest {
    @Test
    public void test() throws Exception {
        File testFile = new File("target/test.txt");
        if (testFile.exists())
            assertTrue(testFile.delete());
        assertTrue(testFile.createNewFile());

        Set<DirectedPseudograph<Integer, MyEdge>> toSave = Sets.newHashSet();
        toSave.add(DirectedPseudographCreator.create(ImmutableMultimap.of(
                1, 2,
                2, 3
        )));
        toSave.add(DirectedPseudographCreator.create(ImmutableMultimap.of(
                3, 2,
                2, 3,
                2, 3
        )));

        GraphListSerializer.save(testFile, toSave);
        assertEquals(toSave, GraphListSerializer.load(testFile));
    }
}
