package LIB;

import java.nio.file.Path;
import java.nio.file.Paths;


public class Main {
    private static final Path filePath = Paths.get("src\\main\\resources\\MyClass.java");
    private static final Path packagePath = Paths.get("src\\main\\resources\\");
    private static final Path projectPath = Paths.get("src\\main\\resources\\");

    public static void main(String[] args) throws Exception {

        DependencyAnalyserLib.getClassDependencies(filePath.toString(), null).onComplete(res -> {
            if(res.failed() && res.result() == null){ System.out.println("Failed " + res.cause().toString());}
            else System.out.println(res.result().getGraph().nodes + "\n" + res.result().getGraph().arcs);
        }).onFailure(Throwable::printStackTrace);


        DependencyAnalyserLib.getPackageDependencies(packagePath.toString()).onComplete(res -> {
            if(res.failed() && res.result() == null){ System.out.println("Failed " + res.cause().toString());}
            else System.out.println(res.result().getGraph().nodes + "\n" + res.result().getGraph().arcs);
        }).onFailure(Throwable::printStackTrace);


        DependencyAnalyserLib.getProjectDependencies(projectPath.toString(), null).onComplete(res -> {
            if(res.failed() && res.result() == null){ System.out.println("Failed " + res.cause().toString());}
            else System.out.println(res.result().getGraph().nodes + "\n" + res.result().getGraph().arcs);
        }).onFailure(Throwable::printStackTrace);
    }
}