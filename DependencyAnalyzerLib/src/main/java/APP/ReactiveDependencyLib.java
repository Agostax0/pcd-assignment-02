package APP;

import LIB.report.ClassDepsReport;
import com.github.javaparser.StaticJavaParser;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.FlowableEmitter;
import shared.DependencyRef;
import shared.DependencyVisitor;
import shared.TreeGraph;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Objects;

public class ReactiveDependencyLib {
    private final static String JAVA_EXTENSION = ".java";

    private ReactiveDependencyLib(){}

    public static Flowable<TreeGraph> generateGraphStream(Path path){


        return Flowable.create(emitter -> {
            new Thread(()->{
                getProjectDependencies(path, emitter);
            }).start();
        }, BackpressureStrategy.BUFFER);
    }

    private static void getProjectDependencies(final Path path, @NonNull FlowableEmitter<TreeGraph> subject ) {
        var file = path.toFile();
        if(file.isDirectory()) {


            File[] listFiles = Objects.requireNonNull(file.listFiles());
            for(int i = 0; i < listFiles.length; i++){
                getProjectDependencies(listFiles[i].toPath(), subject);
            }
        }
        else{
            subject.onNext(getClassDependencies(path).treeGraph);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static ClassDepsReport getClassDependencies(final Path path) {
        var tree = new TreeGraph();

        var file = path.toFile();

        if(file.exists() && file.getName().contains(JAVA_EXTENSION)){
            try{
                var dependencyRef = new DependencyRef();
                new DependencyVisitor().visit(StaticJavaParser.parse(file), dependencyRef);
                tree.addFromRef(dependencyRef, file.getName());
            }
            catch (FileNotFoundException e){
            }
        }
        return new ClassDepsReport(tree);
    }



}
