package org.example;

import com.github.javaparser.StaticJavaParser;

import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class DependencyFrame extends JFrame {
    final Path withJavaUtilImportPath = Paths.get("src\\main\\java\\org\\example\\Main.java");

    public DependencyFrame() {
        this.setSize(800, 600);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        LayoutManager layout = new BorderLayout();
        this.setLayout(layout);

        List<List<String>> importRefs;
        ImportVisitor importVisitor = new ImportVisitor();
        var imports = new ArrayList<ImportRef>();
        try {
            importVisitor.visit(StaticJavaParser.parse(withJavaUtilImportPath.toFile()), imports);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        importRefs = imports.stream().map(ImportRef::getPackageTreeOfImport).toList();
        var tree = new TreeGraph();
        importRefs.forEach(tree::addConnections);


        TreePanel treePanel = new TreePanel(tree);

        this.add(BorderLayout.CENTER, treePanel);
        
        this.setVisible(true);
    }
}
