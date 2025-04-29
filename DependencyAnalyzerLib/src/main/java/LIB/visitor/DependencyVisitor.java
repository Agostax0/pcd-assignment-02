package LIB.visitor;

import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class DependencyVisitor extends VoidVisitorAdapter<DependencyRef> {
    @Override
    public void visit(ImportDeclaration importDeclaration, DependencyRef arg){
        super.visit(importDeclaration, arg);
        arg.importDeclarations.add(importDeclaration);
    }

    @Override
    public void visit(PackageDeclaration packageDeclaration, DependencyRef arg){
        super.visit(packageDeclaration, arg);
        arg.packageDeclaration = packageDeclaration;
    }
}
