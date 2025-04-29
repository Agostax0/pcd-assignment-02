package org.example.report;

import org.example.visitor.TreeBuilder;

public class PackageDepsReport extends DepsReport {
    public PackageDepsReport(final TreeBuilder.TreeGraph treeGraph) {
        super(treeGraph);
    }
}
