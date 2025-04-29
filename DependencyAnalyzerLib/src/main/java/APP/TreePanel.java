package APP;

import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.PublishSubject;
import shared.Pair;
import shared.TreeGraph;
import shared.TreeGraph.GraphNode;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class
TreePanel extends JPanel {

    PublishSubject<TreeGraph> stream;
    private TreeGraph drawnGraph = new TreeGraph();

    public TreePanel(PublishSubject<TreeGraph> graphStream) {
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
                .subscribe(graph -> {
                    drawnGraph.addTree(graph);
                });


    }

    public TreePanel(TreeGraph graph) {
        SwingUtilities.invokeLater(() -> {
            drawnGraph.addTree(graph);
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(Color.WHITE);

        computeNodesPositions();

        g.setColor(Color.BLACK);

        for(var node : drawnGraph.nodes){
            if(node.isLeaf)
                g.setColor(Color.BLUE);
            else
                g.setColor(Color.BLACK);

            g.fillOval(node.x, node.y, 5, 5);
            g.drawString(node.getNodeName(), node.x, node.y - 7);
        }

        for(var arc: drawnGraph.arcs){
            g.drawLine(arc.a.x+2, arc.a.y+2, arc.b.x+2, arc.b.y+2);
        }
    }

    private void computeNodesPositions() {
        var width = this.getWidth();

        var height = this.getHeight();

        int nodeColumnSize = 0;
        int numCols = 0;
        if (!drawnGraph.nodes.isEmpty()) {
            numCols = drawnGraph.nodes.stream().map(GraphNode::getNodeLevel).max(Comparator.naturalOrder()).get();
            nodeColumnSize = width / numCols;
        }

        int nodeRowSize = 0;
        int numRows = 0;
        if (!drawnGraph.arcs.isEmpty()) {
            numRows = drawnGraph.arcs.stream()
                    .map(it -> it.b)
                    .map(graphNode -> new Pair<>(graphNode, drawnGraph.arcs.stream()
                            .filter(node -> node.b.getNodeLevel() == graphNode.getNodeLevel())
                            .count()))
                    .max((a, b) -> Math.toIntExact(a.b - b.b)).get().b.intValue();

            nodeRowSize = height / numRows;
        }


        int verticalOffsetBetweenNodes = nodeRowSize / 2;
        int horizontalOffsetBetweenNodes = nodeColumnSize / 2;
        int numLevels = numCols;

        Map<Integer, List<GraphNode>> orderedTree = new HashMap<>();
        for (int i = 0; i <= numLevels; i++) orderedTree.put(i, new ArrayList<>());

        for (int i = 0; i <= numLevels; i++) {
            int finalI = i;

            var alreadyPresentNodes = orderedTree.get(i);

            var currentLevelNodes = drawnGraph.nodes.stream()
                    .filter(it ->
                            it.getNodeLevel() == finalI && !alreadyPresentNodes.contains(it))
                    .toList(); //list of all nodes in the current level not already added to the map

            if(currentLevelNodes.isEmpty()){
                currentLevelNodes = alreadyPresentNodes;
            }
            else{
                alreadyPresentNodes.addAll(currentLevelNodes);
            }

            for(var currentLevelNode : currentLevelNodes){ //adding each node's children in order
                if(orderedTree.containsKey(i+1)){
                    var childNodes = orderedTree.get(i+1);
                    (drawnGraph.arcs.stream()
                            .filter(it -> it.a.equals(currentLevelNode))
                            .map(it-> it.b)
                            .filter(it -> !childNodes.contains(it)).toList()).forEach(childNodes::add);
                }

            }


        }

        for (int i = 0; i <= numLevels; i++) {
            var orderedNodes = orderedTree.get(i);

            for(int j = 0; j < orderedNodes.size(); j++){
                var currentNode = orderedNodes.get(j);
                currentNode.x = horizontalOffsetBetweenNodes * (i + 1); //based on node level
                currentNode.y = verticalOffsetBetweenNodes * (j + 1); //based on how many nodes have been added to the current level
            }
        }

        System.out.println(orderedTree);

    }
}
