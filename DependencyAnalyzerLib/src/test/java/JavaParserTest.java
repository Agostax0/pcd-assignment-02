import org.example.ImportVisitor;
import org.junit.jupiter.api.BeforeEach;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class JavaParserTest {

    private final Path reversePolishAnnotationPath = Paths.get("D:\\pcd\\DependencyAnalyzerLib\\src\\main\\resources\\ReversePolishNotation.java");
    private final File reversePolishAnnotationFile = reversePolishAnnotationPath.toFile();;

    private ImportVisitor importVisitor;

    @BeforeEach
    void beforeEach(){
    }
}
