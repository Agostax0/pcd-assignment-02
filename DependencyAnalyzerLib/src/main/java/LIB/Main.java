package LIB;
import io.vertx.core.Vertx;

import java.nio.file.Path;
import java.nio.file.Paths;


public class Main {
    private static final Path filePath = Paths.get("C:\\Users\\agost\\IdeaProjects\\pcd-assignment-02\\DependencyAnalyzerLib\\src\\main\\resources\\withjavautil\\ReversePolishNotation.java");
    private static final Path packagePath = Paths.get("src\\main\\java\\");

    public static void main(String[] args) throws Exception {

        DependencyAnalyserLib.getClassDependencies(filePath.toString(), null).onComplete(res -> {
            if(res.failed() && res.result() == null){ System.out.println("Failed " + res.cause().toString());}
            else System.out.println(res.result().getGraph().nodes + "\n" + res.result().getGraph().arcs);
        }).onFailure(Throwable::printStackTrace);

    }
}