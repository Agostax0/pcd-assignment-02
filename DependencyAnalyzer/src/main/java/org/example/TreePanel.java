package org.example;

import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.PublishSubject;
import org.example.TreeGraph.GraphNode;

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

        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(2));

        // Draw arcs
        g2.setColor(Color.BLACK);

        var width = this.getWidth();
        var height = this.getHeight();

        int nodeColumnSize = 0;
        if(!drawnGraph.nodes.isEmpty())
        /**
         *
         * | com 0 | github 1 | javaparser 2 | utils 3 | JavaParser 4 |
         * | java 0 | util 1 | Stack 2 |
         */
            nodeColumnSize = width / drawnGraph.nodes.stream().map(GraphNode::getNodeLevel).max(Comparator.naturalOrder()).get();

        int nodeRowSize = 0;
        if(drawnGraph.arcs.isEmpty()){
            nodeRowSize = 1;//height / Collections.max();
            drawnGraph.nodes.stream().max((n1, n2) -> {
                return Math.toIntExact(drawnGraph.arcs.stream().filter(arc -> arc.b.getNodeLevel() == n1.getNodeLevel()).count())
                        - Math.toIntExact(drawnGraph.arcs.stream().filter(arc -> arc.b.getNodeLevel() == n2.getNodeLevel()).count());
            });

        }



        for(var node : drawnGraph.nodes){



        }

    }
}
