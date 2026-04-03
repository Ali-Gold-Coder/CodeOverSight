package com.codetriage.parser;

import com.codetriage.cli.Config;
import com.codetriage.model.FileInfo;
import com.codetriage.model.MethodSig;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.SimpleName;

import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class FileParser {

    private static final ExecutorService executor = Executors.newSingleThreadExecutor();

    public static Optional<FileInfo> parse(File file, Config config) {

        try {
            Future<Optional<FileInfo>> future = executor.submit( () -> parseInternal(file) );
            return future.get(2, TimeUnit.SECONDS);

        } catch (TimeoutException e){
            System.out.println("Timeout parsing: " + file.getName());
            return Optional.empty();
        } catch (Exception e){
            System.err.println("Parse error: " + file.getName() + " - " + e.getMessage());
            return Optional.empty();
        }
    }

    private static Optional<FileInfo> parseInternal(File file){
        try{
            ParseResult< CompilationUnit> result = new JavaParser().parse(file);
            if(!result.isSuccessful() || result.getResult().isEmpty() ){
                return Optional.empty();
            }

            CompilationUnit cu = result.getResult().get();
            Optional<ClassOrInterfaceDeclaration> classDecl = cu.findFirst(ClassOrInterfaceDeclaration.class);

            if(classDecl.isEmpty()){
                return Optional.empty();
            }

            ClassOrInterfaceDeclaration clazz = classDecl.get();
            String className = clazz.getNameAsString();
            String packageName = cu.getPackageDeclaration().map(p -> p.getNameAsString()).orElse("");

            List<MethodSig> methods = clazz.getMethods().stream().map(FileParser::toMethodSig).collect(Collectors.toList());

            List<String> imports = cu.getImports().stream().map(imp -> imp.getNameAsString()).collect(Collectors.toList());

            return Optional.of(new FileInfo(file.getPath(), className, packageName, methods, imports));


        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private static MethodSig toMethodSig(MethodDeclaration method){

        String params = method.getParameters().stream().map(p -> p.getType().toString()).collect(Collectors.joining(". "));

        String returnType = method.getType().toString();

        return new MethodSig(method.getNameAsString(), params, returnType);


    }
}

