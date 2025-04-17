package org.example;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TreeBuilder {
    public static class TreeNode{
        String name;
        Map<String, TreeNode> leaves = new HashMap<>();

        public TreeNode(String name){
            this.name = name;
        }

        public void addChildren(List<String> children){
            if(!children.isEmpty()){
                String head = children.getFirst();

                TreeNode child = this.leaves.computeIfAbsent(head, TreeNode::new);
                child.addChildren(children.subList(1,children.size()));
            }
        }

        public void print(String prefix) {
            System.out.println(prefix + name);
            for (TreeNode child : leaves.values()) {
                child.print(prefix + "  ");
            }
        }
    }

}
