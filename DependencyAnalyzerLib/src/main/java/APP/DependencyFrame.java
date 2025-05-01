package APP;

import LIB.DependencyAnalyserLib;
import io.reactivex.rxjava3.core.Flowable;
import shared.TreeGraph;

import javax.swing.*;
import java.awt.*;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DependencyFrame extends JFrame {

    public DependencyFrame() {
        this.setSize(1920, 1080);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        TreePanel treePanel = new TreePanel();

        this.add(treePanel);
        this.setVisible(true);
    }
}
