import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.utils.Pair;
import org.example.report.ClassDepsReport;
import org.example.visitor.DependencyRef;
import org.example.visitor.DependencyVisitor;
import org.example.visitor.TreeBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
    private TreeBuilder.TreeGraph tree;

    private List<List<String>> importRefs;

    private void initRefs(Path path) throws FileNotFoundException {
        DependencyVisitor dependencyVisitor = new DependencyVisitor();
        var dependencyRef = new DependencyRef();
        dependencyVisitor.visit(StaticJavaParser.parse(path.toFile()), dependencyRef);
        this.importRefs = dependencyRef.getImportsTrees();
    }

    @BeforeEach
    void beforeEach(){
        this.tree = new TreeBuilder.TreeGraph();
        this.classDepsReport = new ClassDepsReport(this.tree);
    }



    @Test
    void testTreeGraphContainsNodeJava() throws FileNotFoundException {
        initRefs(withJavaUtilImportPath);
        importRefs.forEach(tree::addConnections);

        assertTrue(classDepsReport.getGraph().hasNode("java"));
    }

    @Test
    void testTreeGraphContainsArcJavaUtil() throws FileNotFoundException {
        initRefs(withJavaUtilImportPath);
        importRefs.forEach(tree::addConnections);

        assertTrue(classDepsReport.getGraph().hasArc(new Pair<>("java","util")));
    }

    @Test
    void testTreeGraphDoesNotContainsNodeJava() throws FileNotFoundException {
        initRefs(withoutJavaUtilImportPath);
        importRefs.forEach(tree::addConnections);

        assertFalse(classDepsReport.getGraph().hasNode("java"));

    }
}
