package org.example;

import com.github.javaparser.ast.ImportDeclaration;

public class ImportRef {

    private final ImportDeclaration importDeclaration;

    public ImportRef(final ImportDeclaration importDeclaration){
        this.importDeclaration = importDeclaration;
    }
}
