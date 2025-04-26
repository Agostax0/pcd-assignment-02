package org.example;

import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.PublishSubject;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class TreePanel extends JPanel {

    PublishSubject<TreeGraph> stream;
    private TreeGraph drawnGraph = new TreeGraph();

    public TreePanel(PublishSubject<TreeGraph> graphStream){
        this.stream = graphStream;

        stream
                .observeOn(Schedulers.computation())
                .map(graph -> {

                    var notDrawnNodes = graph.nodes.stream().filter(node -> !drawnGraph.nodes.contains(node)).collect(Collectors.toSet());
                    var notDrawnArcs = graph.arcs.stream().filter(arc -> !drawnGraph.arcs.contains(arc)).collect(Collectors.toSet());

                    var formattedGraph = new TreeGraph();

                    formattedGraph.nodes = notDrawnNodes;
                    formattedGraph.arcs = notDrawnArcs;

                    return formattedGraph;
                })
                .subscribe(graph->{
                    drawnGraph.addTree(graph);
                });
    }

    public TreePanel(TreeGraph graph){
        SwingUtilities.invokeLater(() -> {
            drawnGraph.addTree(graph);
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int verticalSpacing = 100;
        int horizontalSpacing = 80;
        int startY = 50;

        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(2));

        // Draw arcs
        g2.setColor(Color.BLACK);


    }
}
