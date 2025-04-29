package LIB.report;

import shared.TreeGraph;

public class DepsReport {
    public TreeGraph treeGraph;

    protected DepsReport(TreeGraph treeGraph){
        this.treeGraph = treeGraph;
    }

    public TreeGraph getGraph(){
        return treeGraph;
    }
}
