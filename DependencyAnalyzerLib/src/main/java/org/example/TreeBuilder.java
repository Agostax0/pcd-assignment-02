package org.example;

import com.github.javaparser.utils.Pair;
import java.util.*;

public class TreeBuilder {
    public static class TreeNode {
        String name;
        Map<String, TreeNode> leaves = new HashMap<>();

        public TreeNode(String name){
            this.name = name;
        }

        public void addChildren(final List<String> children){
            if(!children.isEmpty()){
                String head = children.getFirst();

                TreeNode child = this.leaves.computeIfAbsent(head, TreeNode::new);
                child.addChildren(children.subList(1,children.size()));
            }
        }

        public Map<String, TreeNode> getLeaves(){
            return this.leaves;
        }

        public boolean isChildrenPresent(final String children){
            return Objects.equals(name, children) || isChildrenInLeaves(children, this.leaves.values());
        }

        private boolean isChildrenInLeaves(String children, Collection<TreeNode> leaves){
            if(leaves == null){
                return false;
            }

            if(leaves.stream().map(leaf -> leaf.name).anyMatch(leafName -> Objects.equals(leafName, children))) {
                return true;
            }

            if(leaves.isEmpty()){
                return false;
            }

            return isChildrenInLeaves(children, leaves.stream().flatMap(leaf -> leaf.leaves.values().stream()).toList());
        }

        @Override
        public String toString() {
            return toStringHelper(0);
        }

        private String toStringHelper(int indentLevel) {
            StringBuilder sb = new StringBuilder();
            String indent = "\t".repeat(indentLevel);
            sb.append(indent).append(name).append("\n");

            for (TreeNode child : leaves.values()) {
                sb.append(child.toStringHelper(indentLevel + 1));
            }

            return sb.toString();
        }
    }

    public static class TreeGraph{
        final Set<GraphNode> nodes = new HashSet<>();
        final Set<Pair<GraphNode,GraphNode>> arcs = new HashSet<>();

        public TreeGraph(){}

        public void addConnections(final List<String> path){
            for(int i = 0; i < path.size(); i++){
                nodes.add(new GraphNode(path.get(i), i));
            }

            for(int i = 0; i < path.size() - 1; i++){
                arcs.add(new Pair<>(new GraphNode(path.get(i), i), new GraphNode(path.get(i + 1), i + 1)));
            }

        }

        public boolean hasNode(final String node){
            return this.nodes.stream().map(it -> it.nodeName).anyMatch(nodeName -> Objects.equals(nodeName, node));
        }

        public Set<Pair<GraphNode, GraphNode>> getArcs() {
            return arcs;
        }

        public Set<GraphNode> getNodes() {
            return nodes;
        }


        public boolean hasArc(Pair<String, String> arc) {
            return arcs.stream().map(it -> new Pair<>(it.a.nodeName, it.b.nodeName)).anyMatch(e -> e.equals(arc));
        }

        public static class GraphNode{
            private final String nodeName;
            private final int nodeLevel;

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
                        '}';
            }
        }
    }

}
