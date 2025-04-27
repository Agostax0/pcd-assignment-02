package org.example;

import java.util.*;
import java.util.stream.Collectors;

public class TreeGraph{
    Set<GraphNode> nodes = new HashSet<>();
    Set<Pair<GraphNode,GraphNode>> arcs = new HashSet<>();

    public TreeGraph(){}

    public void addConnections(final List<String> path){
        List<GraphNode> graphNodes = new ArrayList<>();

        for(int i = 0; i < path.size(); i++){
            graphNodes.add(new GraphNode(path.get(i), i));
        }

        for(int i = 0; i < path.size() - 1; i++){
            arcs.add(new Pair<>(graphNodes.get(i), graphNodes.get(i + 1)));
        }

        nodes.addAll(graphNodes);
    }

    public void addTree(final TreeGraph newTree){
        this.nodes.addAll(newTree.nodes);
        this.arcs.addAll(newTree.arcs);
    }

    public boolean hasNode(final String node){
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

    public static class GraphNode{
        private final String nodeName;
        private final int nodeLevel;

        int x;
        int y;

        public GraphNode(final String nodeName, final int nodeLevel) {
            this.nodeLevel = nodeLevel;
            this.nodeName = nodeName;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            GraphNode graphNode = (GraphNode) o;
            return nodeLevel == graphNode.nodeLevel && Objects.equals(nodeName, graphNode.nodeName);
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
                    ", x=" + x +
                    ", y=" + y +
                    '}';
        }
    }
}
