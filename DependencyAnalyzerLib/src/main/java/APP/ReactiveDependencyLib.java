package APP;

import LIB.report.ClassDepsReport;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.StaticJavaParser;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.FlowableEmitter;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.schedulers.Schedulers;
import shared.DependencyRef;
import shared.DependencyVisitor;
import shared.TreeGraph;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class ReactiveDependencyLib {
    private final static String JAVA_EXTENSION = ".java";

    private ReactiveDependencyLib() {
    }

    public static Flowable<ClassDepsReport> generateGraphStream(Path path) {
        return Flowable.create(emitter -> {
            Schedulers.io().scheduleDirect(()->{
                getProjectDependencies(path, emitter);
                emitter.onComplete();
            });
        }, BackpressureStrategy.BUFFER);
    }

    private static void getProjectDependencies(final Path path, @NonNull FlowableEmitter<ClassDepsReport> subject ) {
        var file = path.toFile();
        if(file.isDirectory()) {


            File[] listFiles = Objects.requireNonNull(file.listFiles());
            for(int i = 0; i < listFiles.length; i++){
                getProjectDependencies(listFiles[i].toPath(), subject);
            }
        }
        else{
            subject.onNext(getClassDependencies(path));
        }
    }

    private static ClassDepsReport getClassDependencies(final Path path) {
        System.out.println(path.toString());
        var tree = new TreeGraph();

        var file = path.toFile();

        if(file.exists() && file.getName().contains(JAVA_EXTENSION)){
            try{

                var config = new ParserConfiguration();
                config.setLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_21);
                StaticJavaParser.setConfiguration(config);
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
