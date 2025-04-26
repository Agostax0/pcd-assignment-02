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
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;


public class Main {
    private static final Path filePath = Paths.get("D:\\pcd\\DependencyAnalyzerLib\\src\\main\\resources\\withjavautil\\ReversePolishNotation.java");
    private static final Path packagePath = Paths.get("D:\\pcd\\DependencyAnalyzerLib\\");

    public static void main(String[] args) throws Exception {

        DependencyAnalyserLib.getProjectDependencies(packagePath.toString()).onComplete(res -> {
            if(res.failed() && res.result() == null){ System.out.println("Failed " + res.cause().toString());}
            else System.out.println(res.result().getGraph().getNodes() + "\n" + res.result().getGraph().getArcs());
        }).onFailure(Throwable::printStackTrace);

        Vertx.vertx().close();
    }
}