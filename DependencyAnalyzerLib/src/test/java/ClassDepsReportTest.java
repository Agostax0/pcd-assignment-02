import com.github.javaparser.StaticJavaParser;
import LIB.report.ClassDepsReport;
import shared.DependencyRef;
import shared.DependencyVisitor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import shared.Pair;
import shared.TreeGraph;

import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ClassDepsReportTest {

    final Path withJavaUtilImportPath = Paths.get("D:\\pcd\\DependencyAnalyzerLib\\src\\main\\resources\\withjavautil\\ReversePolishNotation.java");
    final Path withoutJavaUtilImportPath = Paths.get("D:\\pcd\\DependencyAnalyzerLib\\src\\main\\resources\\withoutjavautil\\Test.java");


    private ClassDepsReport classDepsReport;
    private TreeGraph tree;

    private List<List<String>> refs;

    private void initRefs(Path path) throws FileNotFoundException {
        DependencyVisitor dependencyVisitor = new DependencyVisitor();
        var dependencyRef = new DependencyRef();
        dependencyVisitor.visit(StaticJavaParser.parse(path.toFile()), dependencyRef);
        this.refs = dependencyRef
                .getAllTreesFromFile(
                        withJavaUtilImportPath.getFileName()
                                .toString().split("\\.")[0]);
    }

    @BeforeEach
    void beforeEach(){
        this.tree = new TreeGraph();
        this.classDepsReport = new ClassDepsReport(this.tree);
    }



    @Test
    void testTreeGraphContainsNodeJava() throws FileNotFoundException {
        initRefs(withJavaUtilImportPath);
        refs.forEach(tree::addConnections);

        assertTrue(classDepsReport.getGraph().hasNode("java"));
    }

    @Test
    void testTreeGraphContainsArcJavaUtil() throws FileNotFoundException {
        initRefs(withJavaUtilImportPath);
        refs.forEach(tree::addConnections);

        assertTrue(classDepsReport.getGraph().hasArc(new Pair<>("java","util")));
    }

    @Test
    void testTreeGraphDoesNotContainsNodeJava() throws FileNotFoundException {
        initRefs(withoutJavaUtilImportPath);
        refs.forEach(tree::addConnections);

        assertFalse(classDepsReport.getGraph().hasNode("java"));
    }

    @Test
    void testTreeGraphContainsPackageNodes() throws FileNotFoundException {
        initRefs(withJavaUtilImportPath);
        refs.forEach(tree::addConnections);

        assertTrue(classDepsReport.treeGraph.hasNode("samples"));
    }
}
