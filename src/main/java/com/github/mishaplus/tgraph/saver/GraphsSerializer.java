package com.github.mishaplus.tgraph.saver;

import com.github.mishaplus.tgraph.util.DirectedPseudographCreator;
import com.github.mishaplus.tgraph.util.MyEdge;
import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import org.jgrapht.graph.DirectedPseudograph;

import java.io.*;
import java.util.Set;

public class GraphsSerializer {
    public static void save(File file, Set<DirectedPseudograph<Integer, MyEdge>> graphs)
            throws IOException {
        File transactFile = new File(file.getAbsolutePath() + ".transact");
        if (transactFile.exists())
            Preconditions.checkArgument(transactFile.delete(), "Can't delete transact file");

        if (!file.exists())
            Preconditions.checkArgument(file.createNewFile(), "Can't create file");
        else {
            Preconditions.checkArgument(
                    !file.exists(),
                    "Graphs file %s already exists. Writing ignored",
                    file.getAbsolutePath()
            );
        }

        try (PrintWriter out = new PrintWriter(transactFile)) {
            for (DirectedPseudograph g : graphs)
                out.println(g.toString());
            out.flush();
        }

        Preconditions.checkArgument(
                transactFile.renameTo(file),
                "Can't rename %s to %s",
                transactFile.getAbsolutePath(),
                file.getAbsolutePath()
        );
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
