import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.utils.Pair;
import org.example.ClassDepsReport;
import org.example.ImportRef;
import org.example.ImportVisitor;
import org.example.TreeBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
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
        ImportVisitor importVisitor = new ImportVisitor();
        var imports = new ArrayList<ImportRef>();
        importVisitor.visit(StaticJavaParser.parse(path.toFile()), imports);
        this.importRefs = imports.stream().map(ImportRef::getPackageTreeOfImport).toList();
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
