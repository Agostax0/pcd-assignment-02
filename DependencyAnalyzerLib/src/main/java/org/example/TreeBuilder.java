package org.example;

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
    }

}
