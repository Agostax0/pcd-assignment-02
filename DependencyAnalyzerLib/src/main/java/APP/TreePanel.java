package APP;

import io.reactivex.rxjava3.core.Flowable;
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

    private TreeGraph drawnGraph = new TreeGraph();

    public TreePanel(Flowable<TreeGraph> graphStream) {

        graphStream
                .observeOn(Schedulers.computation())
                .subscribe(graph -> {
                    System.out.println("received graph");
                    SwingUtilities.invokeLater(()->{
                        drawnGraph.addTree(graph);
                        this.repaint();
                    });
                });


    }

    public TreePanel(TreeGraph graph) {
        SwingUtilities.invokeLater(() -> {
            drawnGraph.addTree(graph);
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        System.out.println("paint " + drawnGraph.nodes);
        super.paintComponent(g);
        setBackground(Color.WHITE);

        computeNodesPositions();


        var offsetX = 50;
        var offsetY = 50;

        for(var node : drawnGraph.nodes){
            g.setColor(Color.BLACK);
            if(node.isLeaf)
                g.setColor(Color.BLUE);
            if(node.isPackageNode)
                g.setColor(Color.GREEN);

            node.x += offsetX;
            node.y += offsetY;

            g.fillOval(node.x, node.y, 5, 5);
            g.drawString(node.getNodeName(), node.x, node.y - 7);
        }


        for(var arc: drawnGraph.arcs){
            g.setColor(Color.BLACK);

            GraphNode a = arc.start;

            if(a.x == 0 && a.y == 0){
                final GraphNode tempA = a;
                a = drawnGraph.nodes.stream().filter(it -> it.equals(tempA) && it.x != 0 && it.y != 0).findFirst().get();
            }

            GraphNode b = arc.end;

            if(a.isPackageNode || b.isPackageNode)
                g.setColor(Color.GREEN);

            g.drawLine(a.x+2, a.y+2, b.x+2, b.y+2);
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
                    .map(it -> it.end)
                    .map(graphNode -> new Pair<>(graphNode, drawnGraph.arcs.stream()
                            .filter(node -> node.end.getNodeLevel() == graphNode.getNodeLevel())
                            .count()))
                    .max((a, b) -> Math.toIntExact(a.b - b.b)).get().b.intValue();

            nodeRowSize = height / numRows;
        }


        int verticalOffsetBetweenNodes = (10) + nodeRowSize / 2;
        int horizontalOffsetBetweenNodes = (20) + nodeColumnSize / 2;
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
                    childNodes.addAll((drawnGraph.arcs.stream()
                            .filter(it -> it.start.equals(currentLevelNode))
                            .map(it -> it.end)
                            .filter(it -> !childNodes.contains(it)).toList()));
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

    }
}
