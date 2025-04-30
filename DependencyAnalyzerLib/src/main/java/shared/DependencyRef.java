package shared;

import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.expr.Name;

import java.util.ArrayList;
import java.util.List;

public class DependencyRef {

    public final List<ImportDeclaration> importDeclarations = new ArrayList<>();
    public PackageDeclaration packageDeclaration;
    public DependencyRef(){

    }


    public List<List<String>> getImportsTrees(){
        var res = new ArrayList<List<String>>();
        importDeclarations.forEach(importDeclaration -> res.add(getPackageTreeOfImport(importDeclaration)));
        return res;
    }

    public List<String> getPackageTree(){
        if(packageDeclaration == null)
            return new ArrayList<>();

        if(packageDeclaration.getName().hasQualifier())
            return navigatePackage(packageDeclaration.getName(), new ArrayList<>());
        return List.of(packageDeclaration.getName().getIdentifier());
    }

    private List<String> navigatePackage(Name qualifier, List<String> tree){
        if(qualifier.hasQualifier() && qualifier.getQualifier().isPresent()){
            tree.addFirst(qualifier.getIdentifier());
            navigatePackage(qualifier.getQualifier().get(), tree);
        }else{
            tree.addFirst(qualifier.getIdentifier());
        }
        return tree;
    }


    private List<String> getPackageTreeOfImport(ImportDeclaration importDeclaration){

        if(importDeclaration.getName().hasQualifier())
            return navigateImport(importDeclaration.getName(), new ArrayList<>());
        return List.of(importDeclaration.getName().getIdentifier());
    }

    private List<String> navigateImport(Name qualifier, List<String> tree){
        if(qualifier.hasQualifier() && qualifier.getQualifier().isPresent()){
            tree.addFirst(qualifier.getIdentifier());
            navigateImport(qualifier.getQualifier().get(), tree);
        }else{
            tree.addFirst(qualifier.getIdentifier());
        }
        return tree;
    }
}
