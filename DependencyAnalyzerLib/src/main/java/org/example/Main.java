package org.example;
import com.github.javaparser.*;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.visitor.VoidVisitor;

import java.nio.file.Files;
import java.nio.file.Paths;


public class Main {
    public static void main(String[] args) throws Exception {
        CompilationUnit cu = StaticJavaParser.parse(Files.newInputStream(Paths.get("src/main/resources/ReversePolishNotation.java")));

        VoidVisitor<Void> importVisitor = new ImportVisitor();

        importVisitor.visit(cu, null);
    }
}