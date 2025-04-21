import com.github.javaparser.StaticJavaParser;
import org.example.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PackageDepsReportTest {
    final Path withJavaUtilImportPath = Paths.get("D:\\pcd\\DependencyAnalyzerLib\\src\\main\\resources\\withjavautil\\");
    final Path withoutJavaUtilImportPath = Paths.get("D:\\pcd\\DependencyAnalyzerLib\\src\\main\\resources\\withoutjavautil\\");


    private PackageDepsReport packageDepsReport;
    private TreeBuilder.TreeGraph tree;

    private List<List<String>> importRefs;

    private void initRefs(Path path) throws FileNotFoundException {


        Arrays.stream(Objects.requireNonNull(path.toFile().listFiles())).forEach((file -> {
            ImportVisitor importVisitor = new ImportVisitor();
            var imports = new ArrayList<ImportRef>();
            try {
                importVisitor.visit(StaticJavaParser.parse(file), imports);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            this.importRefs = imports.stream().map(ImportRef::getPackageTreeOfImport).toList();

            var fileTree = new TreeBuilder.TreeGraph();

            importRefs.forEach(fileTree::addConnections);

            this.tree.addTree(fileTree);
        }));
    }

    @BeforeEach
    void beforeEach(){
        this.tree = new TreeBuilder.TreeGraph();
        this.packageDepsReport = new PackageDepsReport(this.tree);
    }

    @Test
    void testTreeGraphIsNotEmpty() throws FileNotFoundException {
        initRefs(withJavaUtilImportPath);

        assertFalse(packageDepsReport.getGraph().getNodes().isEmpty());
    }

    @Test
    void testTreeGraphDoesNotContainJavaUtilImports() throws FileNotFoundException {
        initRefs(withoutJavaUtilImportPath);

        assertFalse(packageDepsReport.getGraph().getNodes().contains("java"));
    }

    @Test
    void testTreeGraphContainsImportsFromDifferentFiles() throws FileNotFoundException {
        initRefs(withoutJavaUtilImportPath);

        assertTrue(packageDepsReport.getGraph().hasNode("StaticJavaParser") && packageDepsReport.getGraph().hasNode("Accessible"));
    }




}
