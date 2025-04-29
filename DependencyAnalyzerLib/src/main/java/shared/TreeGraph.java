package shared;

import java.util.*;
import java.util.stream.Collectors;

public class TreeGraph {
    public Set<GraphNode> nodes = new HashSet<>();
    public Set<Pair<GraphNode,GraphNode>> arcs = new HashSet<>();

    public TreeGraph() {
    }

    public void addConnections(final List<String> path) {
        List<GraphNode> graphNodes = new ArrayList<>();

        for (int i = 0; i < path.size(); i++) {
            graphNodes.add(new GraphNode(path.get(i), i, null, i == path.size() - 1, false));
        }

        for (int i = 0; i < path.size() - 1; i++) {
            var start = graphNodes.get(i);
            var end = graphNodes.get(i + 1);

            GraphNode finalStart = start;
            if (arcs.stream().map(it -> it.a).anyMatch(it -> it.equals(finalStart)))
                start = arcs.stream().map(it -> it.a).filter(it -> it.equals(finalStart)).findFirst().orElse(start);

            GraphNode finalEnd = end;
            if (arcs.stream().map(it -> it.b).anyMatch(it -> it.equals(finalEnd))) {
                end = arcs.stream().map(it -> it.b).filter(it -> it.equals(finalEnd)).findFirst().orElse(end);
            }

            arcs.add(new Pair<>(start, end));
        }

        nodes.addAll(graphNodes);
    }

    public void addFromRef(DependencyRef dependencyRef, final String fileName) {
        Set<GraphNode> importLeafNodes = new HashSet<>();

        List<List<String>> importsTrees = dependencyRef.getImportsTrees();
        for (var importTree : importsTrees) { // [[java,util,List],[java.io.File]]
            List<GraphNode> graphNodes = new ArrayList<>();
            for (int i = 0; i < importTree.size(); i++) { // [java,util,List]
                boolean isLeaf = i == importTree.size() - 1;
                GraphNode newNode = new GraphNode(
                        importTree.get(i),
                        i,
                        i > 0 ? graphNodes.get(i - 1) : null,
                        isLeaf,
                        false
                );
                if(isLeaf) {
                    importLeafNodes.add(newNode);
                }
                graphNodes.add(newNode);
                if(this.nodes.stream().noneMatch(it -> it.equals(newNode))) this.nodes.add(newNode);
            }

            for (int i = 0; i < graphNodes.size() - 1; i++) {
                var start = graphNodes.get(i);
                var end = graphNodes.get(i + 1);

                GraphNode finalStart = start;
                if (arcs.stream().map(it -> it.a).anyMatch(it -> it.equals(finalStart)))
                    start = arcs.stream().map(it -> it.a).filter(it -> it.equals(finalStart)).findFirst().orElse(start);

                GraphNode finalEnd = end;
                if (arcs.stream().map(it -> it.b).anyMatch(it -> it.equals(finalEnd))) {
                    end = arcs.stream().map(it -> it.b).filter(it -> it.equals(finalEnd)).findFirst().orElse(end);
                }

                arcs.add(new Pair<>(start, end));
            }

/*
            Set<GraphNode> distinctNodes = new HashSet<>();
            for(var graphNode: this.nodes) if(graphNodes.stream().distinct().anyMatch(it-> it.equals(graphNode))) distinctNodes.add(graphNode);

            var fatherFulNodes = distinctNodes.stream().filter(graphNode -> graphNode.fatherNode != null).collect(Collectors.toSet());
            for(var graphNode: fatherFulNodes){
                Pair<GraphNode, GraphNode> newArc = new Pair<>(graphNode.fatherNode, graphNode);

                if(this.arcs.stream().noneMatch(it-> it.a.equals(newArc.a) && it.b.equals(newArc.b))) this.arcs.add(newArc);
            }

*/

        }

/*
        var packageTree = dependencyRef.getPackageTree();
        packageTree.addLast(fileName);
        List<GraphNode> packageNodes = new ArrayList<>();
        for(int i = 0; i < packageTree.size(); i++){ // [org.javaparser.samples.(fileName)]
            GraphNode newNode = new GraphNode(
                    packageTree.get(i),
                    i,
                    i > 0 ? packageNodes.get(i - 1) : null,
                    false,
                    true
            );

            packageNodes.add(newNode);
            this.nodes.add(newNode);
        }

        for(var packageNode : packageNodes.stream().filter(it -> it.fatherNode != null).toList()){
            this.arcs.add(new Pair<>(packageNode.fatherNode, packageNode));
        }

        var packageFileNameNode = packageNodes.getLast();

        importLeafNodes.forEach(importLeafNode -> this.arcs.add(new Pair<>(importLeafNode, packageFileNameNode)));
*/
    }

    public void addTree(final TreeGraph newTree) {
        this.nodes.addAll(newTree.nodes);
        this.arcs.addAll(newTree.arcs);
    }

    public boolean hasNode(final String node) {
        return this.nodes.stream().map(it -> it.nodeName).anyMatch(nodeName -> Objects.equals(nodeName, node));
    }

    public Set<Pair<String, String>> getArcs() {
        return this.arcs.stream().map(it -> new Pair<>(it.a.nodeName, it.b.nodeName)).collect(Collectors.toSet());
    }

    public Set<String> getNodes() {
        return this.nodes.stream().map(it -> it.nodeName).collect(Collectors.toSet());
    }


    public boolean hasArc(Pair<String, String> arc) {
        return this.arcs.stream().map(it -> new Pair<>(it.a.nodeName, it.b.nodeName)).anyMatch(e -> e.equals(arc));
    }

    public static class GraphNode {
        private final String nodeName;
        private final int nodeLevel;
        public final GraphNode fatherNode;
        public final boolean isLeaf;
        public boolean isPackageNode;
        public int x;
        public int y;

        public GraphNode(final String nodeName, final int nodeLevel, GraphNode fatherNode, boolean isLeaf, boolean isPackageNode) {
            this.nodeLevel = nodeLevel;
            this.nodeName = nodeName;
            this.fatherNode = fatherNode;
            this.isLeaf = isLeaf;
            this.isPackageNode = isPackageNode;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            GraphNode graphNode = (GraphNode) o;
            return Objects.equals(this.nodeLevel, graphNode.nodeLevel)
                    && this.nodeName.equals(graphNode.nodeName);
        }

        @Override
        public int hashCode() {
            return Objects.hash(nodeName, nodeLevel);
        }

        public String getNodeName() {
            return nodeName;
        }

        public int getNodeLevel() {
            return nodeLevel;
        }


        @Override
        public String toString() {
            return "GraphNode{" +
                    "nodeName='" + nodeName + '\'' +
                    ", nodeLevel=" + nodeLevel +
                    ", fatherNode= " + ((fatherNode != null) ? fatherNode.nodeName : "----" ) +
                    ", x=" + x +
                    ", y=" + y +
                    ", isPackageNode= " + isPackageNode +
                    " }" + '\n';
        }
    }

    public static class GraphArc {
        public GraphNode start;
        public GraphNode end;

        public GraphArc(GraphNode start, GraphNode end) {
            this.start = start;
            this.end = end;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            GraphArc graphArc = (GraphArc) o;
            return start.equals(graphArc.start) && end.equals(graphArc.end);
        }

        @Override
        public int hashCode() {
            return Objects.hash(start, end);
        }
    }
}
