package org.example;

import com.github.javaparser.StaticJavaParser;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.file.FileSystem;
import javassist.NotFoundException;
import org.checkerframework.checker.units.qual.A;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public final class DependencyAnalyserLib {
    private DependencyAnalyserLib(){}
    public static Future<ClassDepsReport> getClassDependencies(String path) {
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

    public static Future<PackageDepsReport> getPackageDependencies(String path){
        Vertx vertx = Vertx.vertx();
        FileSystem fs = vertx.fileSystem();

        return fs.exists(path).compose(doesExist -> {
            if(!doesExist) return Future.failedFuture(new NotFoundException(""));
            return fs.lprops(path);
        }).compose(dirProps -> {
            if(!dirProps.isDirectory()) return Future.failedFuture(new IllegalArgumentException(""));
            return fs.readDir(path);
        }).compose(dirFilePaths -> {

            List<Future> filePaths = new ArrayList<>();

            for (var dirFilePath : dirFilePaths ){
                filePaths.add(
                  fs.lprops(dirFilePath).compose(fileProps -> fileProps.isDirectory() ? Future.failedFuture("") : Future.succeededFuture(dirFilePath))
                );
            }

            return CompositeFuture.all(filePaths).map(pathFutures -> {
                List<String> correctPaths = new ArrayList<>();

                for(int i = 0; i < pathFutures.size(); i++){
                    if(!pathFutures.failed(i)) correctPaths.add(pathFutures.resultAt(i));
                }
                return correctPaths;
            });
        }).compose( filePaths -> {
            List<Future> classReps = new ArrayList<>();

            for(var filePath : filePaths){
                classReps.add(getClassDependencies(filePath));
            }

            return CompositeFuture.all(classReps).map( classReports -> {
                List<ClassDepsReport> reports = new ArrayList<>();

                for(int i = 0; i < classReports.size(); i++){
                    reports.add(classReports.resultAt(i));
                }

                TreeBuilder.TreeGraph treeGraph = new TreeBuilder.TreeGraph();
                PackageDepsReport packageDepsReport = new PackageDepsReport(treeGraph);

                for(var report : reports){
                    treeGraph.addTree(report.treeGraph);
                }

                return packageDepsReport;
            });
        });
    }

    public static Future<ProjectDepsReport> getProjectDependencies(Path path){
        return null;
    }
}
