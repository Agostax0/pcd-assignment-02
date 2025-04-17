package org.example;

import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.List;

public class ImportVisitor extends VoidVisitorAdapter<List<ImportRef>> {
    @Override
    public void visit(ImportDeclaration importDeclaration, List<ImportRef> arg){
        super.visit(importDeclaration, arg);
        arg.add(new ImportRef(importDeclaration));
        //System.out.println(importDeclaration.getName());
    }
}
