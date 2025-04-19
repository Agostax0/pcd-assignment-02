import com.github.javaparser.StaticJavaParser;
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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TreeTest {


    private TreeBuilder.TreeNode tree;

    @BeforeEach
    void beforeEach() throws IOException {
        final Path reversePolishAnnotationPath = Paths.get("D:\\pcd\\DependencyAnalyzerLib\\src\\main\\resources\\ReversePolishNotation.java");
        final File reversePolishAnnotationFile = reversePolishAnnotationPath.toFile();
        ImportVisitor importVisitor = new ImportVisitor();
        var imports = new ArrayList<ImportRef>();
        importVisitor.visit(StaticJavaParser.parse(reversePolishAnnotationFile), imports);
        var importRefs = imports.stream().map(ImportRef::getPackageTreeOfImport).toList();

        this.tree = new TreeBuilder.TreeNode("ROOT");
        importRefs.forEach(tree::addChildren);
    }


    @Test
    void testTreeIsNotEmpty() {
        assertFalse(tree.getLeaves().isEmpty());
    }

    @Test
    void testFindChild() {
        assertTrue(tree.isChildrenPresent("ImportDeclaration"));
    }

    @Test
    void testTreePrint(){
        System.out.println(tree.toString());
    }
}
