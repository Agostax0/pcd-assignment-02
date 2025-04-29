package LIB;
import io.vertx.core.Vertx;

import java.nio.file.Path;
import java.nio.file.Paths;


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