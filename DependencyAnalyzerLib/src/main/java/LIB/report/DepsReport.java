package LIB.report;

import shared.TreeBuilder;

public class DepsReport {
    public TreeBuilder.TreeGraph treeGraph;

    protected DepsReport(TreeBuilder.TreeGraph treeGraph){
        this.treeGraph = treeGraph;
    }

    public TreeBuilder.TreeGraph getGraph(){
        return treeGraph;
    }
}
