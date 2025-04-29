package APP;

import LIB.DependencyAnalyserLib;

import javax.swing.*;
import java.awt.*;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DependencyFrame extends JFrame {
    final Path filePath = Paths.get("src\\main\\resources\\withjavautil\\ReversePolishNotation.java");
    final Path projectPath = Paths.get("C:\\Users\\agost\\IdeaProjects\\pcd-assignment-02\\DependencyAnalyzerLib\\src\\main\\resources\\");

    public DependencyFrame() {
        this.setSize(800, 600);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        LayoutManager layout = new BorderLayout();
        this.setLayout(layout);

        TreePanel treePanel = new TreePanel(ReactiveDependencyLib.generateGraphStream(projectPath));

        this.add(BorderLayout.CENTER, treePanel);

        this.setVisible(true);
    }
}
