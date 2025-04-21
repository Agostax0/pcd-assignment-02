package org.example;

public class PackageDepsReport extends DepsReport {
    public PackageDepsReport(final TreeBuilder.TreeGraph treeGraph) {
        super(treeGraph);
    }

    public TreeBuilder.TreeGraph getGraph(){
        return treeGraph;
    }

}
