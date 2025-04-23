package org.example;

import com.github.javaparser.StaticJavaParser;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.file.FileSystem;
import javassist.NotFoundException;

import javax.lang.model.type.ArrayType;
import java.nio.file.NotDirectoryException;
import java.nio.file.Path;
import java.util.ArrayList;

public final class DependencyAnalyserLib {

    public static Future<ClassDepsReport> getClassDependencies(String path) throws NotFoundException {
        Vertx vertx = Vertx.vertx();
        FileSystem fs = vertx.fileSystem();

        return fs.exists(path).compose(doesExist ->{
            if(!doesExist) return Future.failedFuture(new NotFoundException(""));
            return fs.lprops(path);
        }).compose(fileProps -> {
            if(fileProps.isDirectory()) return Future.failedFuture(new IllegalArgumentException(""));
            return fs.readFile(path);
        }).map(file -> {
            var importRefs = new ArrayList<ImportRef>();
            new ImportVisitor().visit(StaticJavaParser.parse(file.toString()), importRefs);
            var tree = new TreeBuilder.TreeGraph();
            importRefs.stream().map(ImportRef::getPackageTreeOfImport).forEach(tree::addConnections);

            return new ClassDepsReport(tree);
        });
    }

    public static Future<PackageDepsReport> getPackageDependencies(Path path){
        return null;
    }

    public static Future<ProjectDepsReport> getProjectDependencies(Path path){
        return null;
    }
}
