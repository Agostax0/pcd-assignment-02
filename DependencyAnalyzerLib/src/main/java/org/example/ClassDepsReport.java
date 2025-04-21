package org.example;

import static org.example.TreeBuilder.*;

public class ClassDepsReport {
    private final TreeGraph treeGraph;

    public ClassDepsReport(final TreeGraph treeGraph){
        this.treeGraph = treeGraph;
    }
    public TreeGraph getTreeGraph() {
        return this.treeGraph;
    }


}
