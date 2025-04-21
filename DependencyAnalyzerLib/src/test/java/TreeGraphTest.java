import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.utils.Pair;
import org.example.ImportRef;
import org.example.ImportVisitor;
import org.example.TreeBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class TreeGraphTest {


    private TreeBuilder.TreeGraph tree;
    private List<List<String>> importRefs;
    @BeforeEach
    void beforeEach() throws IOException {
        final Path reversePolishAnnotationPath = Paths.get("D:\\pcd\\DependencyAnalyzerLib\\src\\main\\resources\\ReversePolishNotation.java");
        final File reversePolishAnnotationFile = reversePolishAnnotationPath.toFile();
        ImportVisitor importVisitor = new ImportVisitor();
        var imports = new ArrayList<ImportRef>();
        importVisitor.visit(StaticJavaParser.parse(reversePolishAnnotationFile), imports);
        this.importRefs = imports.stream().map(ImportRef::getPackageTreeOfImport).toList();
        this.tree = new TreeBuilder.TreeGraph();
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

    @Test
    void testTreeGraphContainsNode(){
        importRefs.forEach(tree::addConnections);

        assertTrue(tree.getNodes().contains("java"));
    }

    @Test
    void testTreeGraphContainsArch(){
        importRefs.forEach(tree::addConnections);

        assertTrue(tree.getArcs().contains(new Pair<>("java","util")));
    }
}
