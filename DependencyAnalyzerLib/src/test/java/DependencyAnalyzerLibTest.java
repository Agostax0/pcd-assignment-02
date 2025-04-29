import javassist.NotFoundException;
import LIB.DependencyAnalyserLib;
import org.junit.jupiter.api.Test;


import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

public class DependencyAnalyzerLibTest {
    private final Path filePath = Paths.get("src\\main\\resources\\withjavautil\\ReversePolishNotation.java");
    private final Path packagePath = Paths.get("src\\main\\resources\\withjavautil\\");

    private final Path nonExistingFilePath = Paths.get("src\\main\\resources\\withjavautil\\TungTungTungTungSahur.java");

    @Test
    void testClassDependencyOnExistingFile(){
        DependencyAnalyserLib.getClassDependencies(filePath.toString(),null).onComplete(res -> {
            var report = res.result();
        }).onFailure(throwable -> fail());

    }

    @Test
    void testClassDependencyOnNotExistingFile(){
        DependencyAnalyserLib.getClassDependencies(nonExistingFilePath.toString(), null).onComplete(res -> {
            var report = res.result();
        }).onFailure(throwed -> assertInstanceOf(NotFoundException.class, throwed));
    }


    @Test
    void testCorrectClassDependencyNodes(){
        DependencyAnalyserLib.getClassDependencies(filePath.toString(), null).onComplete(res -> {
            System.out.println(res.result().getGraph().getNodes());
        });
    }

    @Test
    void testCorrectPackageDependencyNodes(){
        DependencyAnalyserLib.getPackageDependencies(packagePath.toString()).onComplete(res -> {
            System.out.println(res.result().getGraph().getNodes());
        }).onFailure(Throwable::printStackTrace);
    }
}
