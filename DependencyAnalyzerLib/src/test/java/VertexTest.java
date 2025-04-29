import io.vertx.core.Vertx;
import io.vertx.core.file.FileSystem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class VertexTest {

    private Vertx vertx = Vertx.vertx();

    private Path srcPath = Paths.get("src\\main\\resources\\");
    private Path projPath = Paths.get("src\\");
    private Path packagePath = Paths.get("src\\main\\resources\\withoutjavautil");
    private Path filePath = Paths.get("src\\main\\resources\\withjavautil\\ReversePolishNotation.java");


    private FileSystem fs;

    @BeforeEach
    void init(){
        fs = vertx.fileSystem();
    }

    @Test
    void testReadFile(){
        fs.readFile(filePath.toAbsolutePath().toString()).onComplete(res -> {
            var file = res.result();
            assertNotNull(file);
        });
    }

    @Test
    void testReadPackage(){
        fs.readDir(packagePath.toAbsolutePath().toString()).onComplete(res -> {
            var files = res.result();
            assertNotNull(files);
        });
    }

    @Test
    void testReadSrc(){
        fs.readDir(srcPath.toAbsolutePath().toString()).onComplete(res -> {
            res.result().forEach(file -> {
                        fs.lprops(file).onComplete(props -> {
                            if(props.result().isDirectory()){
                                fs.readDir(file).onComplete(dirRes -> {
                                    var files = dirRes.result();
                                    assertNotNull(files);
                                });
                            }
                            else{
                                fs.readFile(filePath.toAbsolutePath().toString()).onComplete(fileRes -> {
                                    var fileResult = fileRes.result();
                                    assertNotNull(fileResult);
                                });
                            }
                        });
            });
        });
    }



}
