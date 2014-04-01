package com.github.mishaplus.tgraph;

import org.jgraph.JGraph;
import org.jgrapht.Graph;
import org.jgrapht.ext.JGraphModelAdapter;

import javax.swing.*;
import java.util.Scanner;

/**
 * Created by michael on 01.04.14
 */
public class Shower {
    public static <V, E> void show(Graph<V, E> graph) throws InterruptedException {
        JFrame frame = new JFrame();
        frame.setSize(400, 400);
        JGraph jgraph = new JGraph(new JGraphModelAdapter(graph));
        frame.getContentPane().add(jgraph);
        frame.setVisible(true);
        while (true) {
            if (!frame.isVisible())
                break;
            Thread.sleep(500);
        }
        /*while (true) {
            String in = new Scanner(System.in).nextLine();
            if (in.contains("a"))
                break;

        }*/
    }
}
