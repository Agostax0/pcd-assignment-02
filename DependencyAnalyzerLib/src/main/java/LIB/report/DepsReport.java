package LIB.report;

import LIB.visitor.TreeBuilder;

public class DepsReport {
    public TreeBuilder.TreeGraph treeGraph;

    protected DepsReport(TreeBuilder.TreeGraph treeGraph){
        this.treeGraph = treeGraph;
    }

    public TreeBuilder.TreeGraph getGraph(){
        return treeGraph;
    }
}
