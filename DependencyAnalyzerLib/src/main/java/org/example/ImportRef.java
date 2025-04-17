package org.example;

import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.expr.Name;

import java.util.ArrayList;
import java.util.List;

public class ImportRef {

    private final ImportDeclaration importDeclaration;

    public ImportRef(final ImportDeclaration importDeclaration){
        this.importDeclaration = importDeclaration;
    }

    public List<String> getPackageTreeOfImport(){

        if(importDeclaration.getName().hasQualifier())
            return navigateImport(importDeclaration.getName(), new ArrayList<>());
        return List.of(importDeclaration.getName().getIdentifier());
    }

    private List<String> navigateImport(Name qualifier, List<String> tree){
        if(qualifier.hasQualifier() && qualifier.getQualifier().isPresent()){
            tree.addLast(qualifier.getIdentifier());
            navigateImport(qualifier.getQualifier().get(), tree);
        }else{
            tree.addLast(qualifier.getIdentifier());
        }
        return tree;
    }
}
