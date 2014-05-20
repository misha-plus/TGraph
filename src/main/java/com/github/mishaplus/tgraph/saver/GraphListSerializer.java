package com.github.mishaplus.tgraph.saver;

import com.github.mishaplus.tgraph.util.DirectedPseudographCreator;
import com.github.mishaplus.tgraph.util.MyEdge;
import com.google.common.collect.Sets;
import org.jgrapht.graph.DirectedPseudograph;

import java.io.*;
import java.util.Set;

public class GraphListSerializer {
    public static void save(File file, Set<DirectedPseudograph<Integer, MyEdge>> graphs)
            throws IOException {
        try (PrintWriter out = new PrintWriter(file)) {
            for (DirectedPseudograph g : graphs)
                out.println(g.toString());
            out.flush();
        }
    }

    public static Set<DirectedPseudograph<Integer, MyEdge>> load(File file) throws IOException {
        try (BufferedReader reader
                     = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
            Set<DirectedPseudograph<Integer, MyEdge>> result = Sets.newHashSet();
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().equals(""))
                    continue;
                result.add(DirectedPseudographCreator.fromString(line.trim()));
            }
            return result;
        }
    }
}
