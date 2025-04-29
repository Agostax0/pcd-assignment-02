package APP;

import LIB.DependencyAnalyserLib;
import com.github.javaparser.StaticJavaParser;
import shared.DependencyRef;
import shared.DependencyVisitor;
import shared.TreeGraph;

import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class DependencyFrame extends JFrame {
    final Path withJavaUtilImportPath = Paths.get("src\\main\\resources\\withjavautil\\ReversePolishNotation.java");
    final Path projectPath = Paths.get("src\\main\\");

    public DependencyFrame() {
        this.setSize(800, 600);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        LayoutManager layout = new BorderLayout();
        this.setLayout(layout);
//
//        DependencyVisitor importVisitor = new DependencyVisitor();
//        var dependencyRef = new DependencyRef();
//        try {
//            importVisitor.visit(StaticJavaParser.parse(withJavaUtilImportPath.toFile()), dependencyRef);
//        } catch (FileNotFoundException e) {
//            throw new RuntimeException(e);
//        }
//
//        System.out.println(dependencyRef.getPackageTree());
//
//        var tree = new TreeGraph();
//
//        dependencyRef.getImportsTrees().forEach(tree::addConnections);
//
//
//        TreePanel treePanel = new TreePanel(tree);
//
//        this.add(BorderLayout.CENTER, treePanel);
//
//        this.setVisible(true);

        DependencyAnalyserLib.getProjectDependencies(projectPath.toAbsolutePath().toString(), null).onComplete(res->{
            TreePanel treePanel = new TreePanel(res.result().getGraph());

            this.add(BorderLayout.CENTER, treePanel);

            this.setVisible(true);

        }).onFailure(Throwable::printStackTrace);
    }
}
