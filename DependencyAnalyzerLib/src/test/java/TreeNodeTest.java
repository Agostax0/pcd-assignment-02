import com.github.javaparser.StaticJavaParser;
import org.example.visitor.DependencyRef;
import org.example.visitor.DependencyVisitor;
import org.example.visitor.TreeBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TreeNodeTest {


    private TreeBuilder.TreeNode tree;

    @BeforeEach
    void beforeEach() throws IOException {
        final Path reversePolishAnnotationPath = Paths.get("D:\\pcd\\DependencyAnalyzerLib\\src\\main\\resources\\withjavautil\\ReversePolishNotation.java");
        final File reversePolishAnnotationFile = reversePolishAnnotationPath.toFile();
        DependencyVisitor dependencyVisitor = new DependencyVisitor();
        var dependencyRef = new DependencyRef();
        dependencyVisitor.visit(StaticJavaParser.parse(reversePolishAnnotationFile), dependencyRef);
        var importRefs = dependencyRef.getImportsTrees();

        this.tree = new TreeBuilder.TreeNode("ROOT");
        importRefs.forEach(tree::addChildren);
    }


    @Test
    void testTreeIsNotEmpty() {
        assertFalse(tree.getLeaves().isEmpty());
    }

    @Test
    void testFindChild() {
        assertTrue(tree.isChildrenPresent("Stack"));
    }

    @Test
    void testTreePrint(){
        System.out.println(tree.toString());
    }
}
