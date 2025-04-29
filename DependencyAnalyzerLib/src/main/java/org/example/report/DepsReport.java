package org.example.report;

import org.example.visitor.TreeBuilder;

public class DepsReport {
    protected TreeBuilder.TreeGraph treeGraph;

    protected DepsReport(TreeBuilder.TreeGraph treeGraph){
        this.treeGraph = treeGraph;
    }

    public TreeBuilder.TreeGraph getGraph(){
        return treeGraph;
    }
}
