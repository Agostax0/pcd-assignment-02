package LIB.report;

import LIB.visitor.TreeBuilder;

public class ProjectDepsReport extends DepsReport{
    public ProjectDepsReport(TreeBuilder.TreeGraph tree) {
        super(tree);
    }
}
