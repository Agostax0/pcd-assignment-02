import com.github.javaparser.StaticJavaParser;
import org.example.ImportRef;
import org.example.ImportVisitor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JavaParserTest {

    private final Path reversePolishAnnotationPath = Paths.get("D:\\pcd\\DependencyAnalyzerLib\\src\\main\\resources\\ReversePolishNotation.java");
    private final File reversePolishAnnotationFile = reversePolishAnnotationPath.toFile();

    private ImportVisitor importVisitor;

    @BeforeEach
    void beforeEach() throws IOException {
        importVisitor = new ImportVisitor();
    }

    @Test
    void testReversePolishAnnotationImportsIsFive() throws FileNotFoundException {
        var imports = new ArrayList<ImportRef>();
        importVisitor.visit(StaticJavaParser.parse(reversePolishAnnotationFile), imports);
        /*
        import java.util.Stack;
        import java.util.stream.Stream;
        import java.util.HashMap;
        import java.util.concurrent.AbstractExecutorService;
        import java.io.IOException;
         */
        assertEquals(5, imports.size());
    }

    @Test
    void testReversePolishAnnotationImportTree() throws FileNotFoundException {
        var imports = new ArrayList<ImportRef>();
        importVisitor.visit(StaticJavaParser.parse(reversePolishAnnotationFile), imports);

        imports.forEach(importRef -> System.out.println(importRef.getPackageTreeOfImport().toString()));

    }
}
