package org.example;
import com.github.javaparser.*;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.visitor.VoidVisitor;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.file.AsyncFile;
import io.vertx.core.file.AsyncFileLock;
import io.vertx.core.file.OpenOptions;

import java.io.Closeable;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;


public class Main {
    public static void main(String[] args) throws Exception {
        CompilationUnit cu = StaticJavaParser.parse(
                Files.newInputStream(
                        Paths.get("src/main/resources/ReversePolishNotation.java")
                )
        );

        Vertx vertx = Vertx.vertx();

        var path = Paths.get("D:\\pcd\\DependencyAnalyzerLib\\src\\main\\resources\\ReversePolishNotation.java");

        var fs = vertx.fileSystem();

        fs.readFile(path.toAbsolutePath().toString()).onComplete(res -> {
            var file = res.result().getBytes().toString();
            new ImportVisitor().visit(StaticJavaParser.parse(file), null);
        });

        VoidVisitor<Void> importVisitor = new ImportVisitor();
        importVisitor.visit(cu, null);



        System.exit(0);

    }
}