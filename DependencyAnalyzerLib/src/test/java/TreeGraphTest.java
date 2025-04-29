import com.github.javaparser.StaticJavaParser;
import shared.DependencyRef;
import shared.DependencyVisitor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import shared.TreeGraph;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class TreeGraphTest {


    private TreeGraph tree;
    private List<List<String>> importRefs;
    @BeforeEach
    void beforeEach() throws IOException {
        final Path reversePolishAnnotationPath = Paths.get("D:\\pcd\\DependencyAnalyzerLib\\src\\main\\resources\\withjavautil\\ReversePolishNotation.java");
        final File reversePolishAnnotationFile = reversePolishAnnotationPath.toFile();
        DependencyVisitor dependencyVisitor = new DependencyVisitor();
        var dependencyRefs = new DependencyRef();
        dependencyVisitor.visit(StaticJavaParser.parse(reversePolishAnnotationFile), dependencyRefs);
        this.importRefs = dependencyRefs.getImportsTrees();
        this.tree = new TreeGraph();
    }

    @Test
    void testTreeGraphIsEmpty(){
        assertTrue(tree.getArcs().isEmpty() && tree.getNodes().isEmpty());
    }

    @Test
    void testTreeGraphIsNotEmptyAfterAddingConnections(){
        tree.addConnections(importRefs.getFirst());

        assertTrue(!tree.getArcs().isEmpty() && !tree.getNodes().isEmpty());
    }
}
