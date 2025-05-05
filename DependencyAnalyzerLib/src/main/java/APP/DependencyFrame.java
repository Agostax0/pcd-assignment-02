package APP;

import javax.swing.*;

public class DependencyFrame extends JFrame {

    public DependencyFrame() {
        this.setSize(1200, 800);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        TreePanel treePanel = new TreePanel();

        this.add(treePanel);
        this.setVisible(true);
    }
}
