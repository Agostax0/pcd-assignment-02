package org.example.report;

import org.example.visitor.TreeBuilder;

public class ProjectDepsReport extends DepsReport{
    public ProjectDepsReport(TreeBuilder.TreeGraph tree) {
        super(tree);
    }
}
