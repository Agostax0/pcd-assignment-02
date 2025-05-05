package APP;

import LIB.report.ClassDepsReport;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import shared.Pair;
import shared.TreeGraph;
import shared.TreeGraph.GraphNode;

import javax.swing.*;
import java.awt.*;
import java.nio.file.Path;
import java.util.*;
import java.util.List;

public class
TreePanel extends JPanel {
    public static final String START = "Start";
    public static final String RUNNING = "Running";
    private TreeGraph drawnGraph = new TreeGraph();
    private static String CLASSES_ANALYZED = "Classes Analyzed: ";
    private static String DEPENDENCIES_FOUND = "Dependencies Found: ";
    private int classesAnalyzed = 0;

    private Flowable<ClassDepsReport> subscribed;

    public TreePanel() {
        this.setLayout(new BorderLayout());

        var toolBox = new JPanel();
        toolBox.setLayout(new GridLayout(1, 4));

        var pathSelector = new JTextArea();
        pathSelector.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        var startBtn = new JButton("Start");
        var classesAnalyzed = new JLabel(CLASSES_ANALYZED + 0);
        classesAnalyzed.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        var dependenciesFound = new JLabel(DEPENDENCIES_FOUND + 0);
        dependenciesFound.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        startBtn.addActionListener((l) -> {
            drawnGraph.nodes.clear();
            drawnGraph.arcs.clear();

            if(startBtn.getText().equals(START)){
                startBtn.setText(RUNNING);
                subscribed = ReactiveDependencyLib.generateGraphStream(Path.of(pathSelector.getText()));
                subscribed
                        .subscribeOn(Schedulers.computation())
                        .doOnComplete(() ->{
                            startBtn.setText(START);
                        })
                        .subscribe(classDepsReport -> {
                            SwingUtilities.invokeLater(() -> {
                                drawnGraph.addTree(classDepsReport.treeGraph);
                                this.classesAnalyzed++;
                                classesAnalyzed.setText(CLASSES_ANALYZED + this.classesAnalyzed);
                                dependenciesFound.setText(DEPENDENCIES_FOUND + drawnGraph.nodes.stream().filter(it -> !it.isPackageNode).count() + "");
                                this.repaint();
                            });
                        });
            }
        });

        toolBox.add(pathSelector);
        toolBox.add(startBtn);
        toolBox.add(classesAnalyzed);
        toolBox.add(dependenciesFound);

        this.add(BorderLayout.SOUTH,toolBox);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(Color.WHITE);

        computeNodesPositions();

        var offsetX = 30;
        var offsetY = 30;

        for (var node : drawnGraph.nodes) {
            g.setColor(Color.BLACK);
            if (node.isLeaf)
                g.setColor(Color.BLUE);
            if (node.isPackageNode)
                g.setColor(Color.GREEN);

            node.x += offsetX;
            node.y += offsetY;

            g.fillOval(node.x, node.y, 5, 5);
            g.drawString(node.getNodeName(), node.x, node.y - 7);
        }


        for (var arc : drawnGraph.arcs) {
            g.setColor(Color.BLACK);

            GraphNode a = arc.start;

            if (a.x == 0 && a.y == 0) {
                final GraphNode tempA = a;
                a = drawnGraph.nodes.stream().filter(it -> it.equals(tempA) && it.x != 0 && it.y != 0).findFirst().get();
            }

            GraphNode b = arc.end;
            drawArrowLine(g, a.x + 3, a.y +3 , b.x +3 , b.y +3 , 5, 10);
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

            if (currentLevelNodes.isEmpty()) {
                currentLevelNodes = alreadyPresentNodes;
            } else {
                alreadyPresentNodes.addAll(currentLevelNodes);
            }

            for (var currentLevelNode : currentLevelNodes) { //adding each node's children in order
                if (orderedTree.containsKey(i + 1)) {
                    var childNodes = orderedTree.get(i + 1);
                    childNodes.addAll((drawnGraph.arcs.stream()
                            .filter(it -> it.start.equals(currentLevelNode))
                            .map(it -> it.end)
                            .filter(it -> !childNodes.contains(it)).toList()));
                }

            }


        }

        for (int i = 0; i <= numLevels; i++) {
            var orderedNodes = orderedTree.get(i);

            for (int j = 0; j < orderedNodes.size(); j++) {
                var currentNode = orderedNodes.get(j);
                currentNode.x = horizontalOffsetBetweenNodes * (i + 1); //based on node level
                currentNode.y = verticalOffsetBetweenNodes * (j + 1); //based on how many nodes have been added to the current level
            }
        }
    }

    private void drawArrowLine(Graphics g, int x1, int y1, int x2, int y2, int d, int h) {
        int dx = x2 - x1, dy = y2 - y1;
        double D = Math.sqrt(dx*dx + dy*dy);
        double xm = D - d, xn = xm, ym = h, yn = -h, x;
        double sin = dy / D, cos = dx / D;

        x = xm*cos - ym*sin + x1;
        ym = xm*sin + ym*cos + y1;
        xm = x;

        x = xn*cos - yn*sin + x1;
        yn = xn*sin + yn*cos + y1;
        xn = x;

        int[] xpoints = {x2, (int) xm, (int) xn};
        int[] ypoints = {y2, (int) ym, (int) yn};

        g.drawLine(x1, y1, x2, y2);
        g.fillPolygon(xpoints, ypoints, 3);
    }
}
