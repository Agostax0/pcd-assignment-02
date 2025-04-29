package LIB;

import com.github.javaparser.StaticJavaParser;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.file.FileSystem;
import javassist.NotFoundException;
import LIB.report.ClassDepsReport;
import LIB.report.PackageDepsReport;
import LIB.report.ProjectDepsReport;
import shared.DependencyRef;
import shared.DependencyVisitor;
import shared.TreeGraph;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public final class DependencyAnalyserLib {
    private final static String JAVA_EXTENSION = ".java";
    private DependencyAnalyserLib() {
    }

    public static Future<ClassDepsReport> getClassDependencies(final String path, final FileSystem contextFs) {
        FileSystem fs = (contextFs == null) ? Vertx.vertx().fileSystem() : contextFs;

        return fs.exists(path).compose(doesExist -> {
            if (!doesExist) return Future.failedFuture(new NotFoundException(""));
            return fs.lprops(path);
        }).compose(fileProps -> {
            if (fileProps.isDirectory()) return Future.failedFuture(new IllegalArgumentException(""));
            return fs.readFile(path);
        }).map(file ->
        {
            var tree = new TreeGraph();

            if(path.contains(JAVA_EXTENSION)){
                var dependencyRef = new DependencyRef();
                new DependencyVisitor().visit(StaticJavaParser.parse(file.toString()), dependencyRef);
                dependencyRef.getImportsTrees()
                        .forEach(tree::addConnections);
            }
            else{
                System.out.println("Filtered Through " + path);
            }

            return new ClassDepsReport(tree);
        });
    }

    public static Future<PackageDepsReport> getPackageDependencies(final String path) {
        Vertx vertx = Vertx.vertx();
        FileSystem fs = vertx.fileSystem();

        return fs.exists(path).compose(doesExist -> {
            if (!doesExist || !path.contains(JAVA_EXTENSION)) return Future.failedFuture(new NotFoundException(""));
            return fs.lprops(path);
        }).compose(dirProps -> {
            if (!dirProps.isDirectory()) return Future.failedFuture(new IllegalArgumentException(""));
            return fs.readDir(path, "*.java");
        }).compose(dirFilePaths -> {

            List<Future<String>> filePaths = new ArrayList<>();

            for (var dirFilePath : dirFilePaths) {
                filePaths.add(
                        fs.lprops(dirFilePath).compose(fileProps -> fileProps.isDirectory() ? Future.failedFuture("") : Future.succeededFuture(dirFilePath))
                );
            }
            return Future.all(filePaths).map(pathFutures -> {
                List<String> correctPaths = new ArrayList<>();

                for (int i = 0; i < pathFutures.size(); i++) {
                    if (!pathFutures.failed(i)) correctPaths.add(pathFutures.resultAt(i));
                }
                return correctPaths;
            });
        }).compose(filePaths -> {
            List<Future<ClassDepsReport>> classReps = new ArrayList<>();

            for (var filePath : filePaths) {
                classReps.add(getClassDependencies(filePath, fs));
            }

            return Future.all(classReps).map(classReports -> {
                List<ClassDepsReport> reports = new ArrayList<>();

                for (int i = 0; i < classReports.size(); i++) {
                    reports.add(classReports.resultAt(i));
                }

                TreeGraph treeGraph = new TreeGraph();
                PackageDepsReport packageDepsReport = new PackageDepsReport(treeGraph);

                for (var report : reports) {
                    treeGraph.addTree(report.treeGraph);
                }

                return packageDepsReport;
            });
        });
    }

    public static Future<ProjectDepsReport> getProjectDependencies(final String path, final FileSystem contextFs) {
        FileSystem fs = (contextFs == null) ? Vertx.vertx().fileSystem() : contextFs;

        return fs.exists(path)
                .compose(doesExist -> { //mi accerto che il path esista
                    if (doesExist) return fs.lprops(path);
                    else return Future.failedFuture(path + "does not exist");
                })
                .compose(pathProps -> {
                    if (!pathProps.isDirectory()) Future.failedFuture(path + " is Not Directory");
                    return fs.readDir(path);
                })
                .compose(elemPaths -> {
                    List<Future<TreeGraph>> depsList = new ArrayList<>();
                    for (var elemPath : elemPaths) {

                        depsList.add(fs.lprops(elemPath).compose(elemProps -> {
                            if (!elemProps.isDirectory()){
                                return getClassDependencies(elemPath, fs).map(rep -> rep.treeGraph);
                            }
                            else {
                                return getProjectDependencies(elemPath, fs).map(rep -> rep.treeGraph);
                            }
                        }));
                    }
                    return Future.all(depsList).map(fileReps -> {
                        List<TreeGraph> graphs = new ArrayList<>();
                        for (int i = 0; i < fileReps.size(); i++) {
                            if (fileReps.succeeded(i)) graphs.add(fileReps.resultAt(i));
                        }
                        return graphs;
                    });
                }).map(graphs -> {
                    ProjectDepsReport report = new ProjectDepsReport(new TreeGraph());

                    for (var graph : graphs) report.treeGraph.addTree(graph);

                    return report;
                });
    }
}
