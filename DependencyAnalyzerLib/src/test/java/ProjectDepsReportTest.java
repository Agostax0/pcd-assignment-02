import com.github.javaparser.StaticJavaParser;
import LIB.report.ProjectDepsReport;
import shared.DependencyRef;
import shared.DependencyVisitor;
import shared.TreeBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class ProjectDepsReportTest {
    final Path srcPath = Paths.get("src\\main\\resources\\");


    private ProjectDepsReport projectDepsReport;
    private TreeBuilder.TreeGraph tree;

    private List<List<String>> importRefs;

    private void initRefs(Path path) throws FileNotFoundException {


        Arrays.stream(Objects.requireNonNull(path.toFile().listFiles())).forEach((file -> {
            if(file.isDirectory()){
                try {
                    initRefs(file.toPath());
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
            else{
                DependencyVisitor dependencyVisitor = new DependencyVisitor();
                var dependencyRefs = new DependencyRef();
                try {
                    dependencyVisitor.visit(StaticJavaParser.parse(file), dependencyRefs);
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
                this.importRefs = dependencyRefs.getImportsTrees();

                var fileTree = new TreeBuilder.TreeGraph();

                importRefs.forEach(fileTree::addConnections);

                this.tree.addTree(fileTree);
            }
        }));
    }

    @BeforeEach
    void beforeEach(){
        this.tree = new TreeBuilder.TreeGraph();
        this.projectDepsReport = new ProjectDepsReport(this.tree);
    }

    @Test
    void testContainsNodesFromDifferentFolders() throws FileNotFoundException {
        initRefs(srcPath);

        System.out.println(this.projectDepsReport.getGraph().getNodes());

        assertTrue(this.projectDepsReport.getGraph().hasNode("util") && this.projectDepsReport.getGraph().hasNode("Accessible"));
    }

}
