import com.github.javaparser.StaticJavaParser;
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
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class JavaParserTest {

    private final Path reversePolishAnnotationPath = Paths.get("D:\\pcd\\DependencyAnalyzerLib\\src\\main\\resources\\ReversePolishNotation.java");
    private final File reversePolishAnnotationFile = reversePolishAnnotationPath.toFile();

    private ImportVisitor importVisitor;

    @BeforeEach
    void beforeEach() throws IOException {
        importVisitor = new ImportVisitor();
    }

    @Test
    void testReversePolishAnnotationImportsIsNotEmpty() throws FileNotFoundException {
        var imports = new ArrayList<ImportRef>();
        importVisitor.visit(StaticJavaParser.parse(reversePolishAnnotationFile), imports);
        /*
        import java.util.Stack;
        import java.util.stream.Stream;
        import java.util.HashMap;
        import java.util.concurrent.AbstractExecutorService;
        import java.io.IOException;
         */
        assertFalse(imports.isEmpty());
    }

    @Test
    void testReversePolishAnnotationImportTreeIsNotEmpty() throws FileNotFoundException {
        var imports = new ArrayList<ImportRef>();
        importVisitor.visit(StaticJavaParser.parse(reversePolishAnnotationFile), imports);
        assertFalse(imports.stream().map(ImportRef::getPackageTreeOfImport).collect(Collectors.toList()).isEmpty());
    }

    @Test
    void testTree() throws FileNotFoundException {
        var imports = new ArrayList<ImportRef>();
        importVisitor.visit(StaticJavaParser.parse(reversePolishAnnotationFile), imports);
        var importRefs = imports.stream().map(ImportRef::getPackageTreeOfImport).toList();
        /**
         * [java, util, Stack]
         * [java, util, stream, Stream]
         * [java, util, HashMap]
         * [java, util, concurrent, AbstractExecutorService]
         * [java, io, IOException]
         * [com, github, javaparser, ast, ImportDeclaration]
         */

        /// ->

        /**
         * [{java,com},{util,io,github},(Stack,stream,HashMap,concurrent,IOException,javaparser),(Stream, AbstractExecutorService,ast),(ImportDeclaration))]
         */

        TreeBuilder.TreeNode tree = new TreeBuilder.TreeNode("ROOT");

        importRefs.forEach(tree::addChildren);

        tree.print("");
    }
}
