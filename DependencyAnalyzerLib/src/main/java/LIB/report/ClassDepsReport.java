package LIB.report;

import shared.TreeGraph;

public class ClassDepsReport extends DepsReport{
    public final String className;
    public ClassDepsReport(final TreeGraph treeGraph){
        super(treeGraph);
        className = "";
    }
    public ClassDepsReport(final TreeGraph treeGraph, final String className){
        super(treeGraph);
        this.className = className;
    }
}
