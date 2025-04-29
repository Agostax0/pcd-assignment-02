package org.example.report;

import static org.example.visitor.TreeBuilder.*;

public class ClassDepsReport extends DepsReport{

    public ClassDepsReport(final TreeGraph treeGraph){
        super(treeGraph);
    }
}
