import com.github.javaparser.StaticJavaParser;
import LIB.visitor.DependencyRef;
import LIB.visitor.DependencyVisitor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

public class JavaParserTest {

    private final Path reversePolishAnnotationPath = Paths.get("src\\main\\resources\\withjavautil\\ReversePolishNotation.java");
    private final File reversePolishAnnotationFile = reversePolishAnnotationPath.toFile();

    private DependencyVisitor dependencyVisitor;

    @BeforeEach
    void beforeEach() throws IOException {
        dependencyVisitor = new DependencyVisitor();
    }

    @Test
    void testReversePolishAnnotationImportsIsNotEmpty() throws FileNotFoundException {
        var dependencyRef = new DependencyRef();
        dependencyVisitor.visit(StaticJavaParser.parse(reversePolishAnnotationFile), dependencyRef);
        assertFalse(dependencyRef.importDeclarations.isEmpty());
    }

    @Test
    void testReversePolishAnnotationPackageIsNotNull() throws FileNotFoundException {
        var dependencyRef = new DependencyRef();
        dependencyVisitor.visit(StaticJavaParser.parse(reversePolishAnnotationFile), dependencyRef);
        assertNotNull(dependencyRef.packageDeclaration);
    }

    @Test
    void testReversePolishAnnotationPackageIsNotEmpty() throws FileNotFoundException{
        var dependencyRef = new DependencyRef();
        dependencyVisitor.visit(StaticJavaParser.parse(reversePolishAnnotationFile), dependencyRef);
        System.out.println(reversePolishAnnotationPath.getFileName().toString());

        assertFalse(dependencyRef.getPackageTree().isEmpty());
    }


}
