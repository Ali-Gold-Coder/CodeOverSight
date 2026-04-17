package com.codetriage.parser;

import com.codetriage.cli.Config;
import com.codetriage.model.FileInfo;
import com.codetriage.model.MethodSig;
import com.codetriage.model.ClassInfo;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.SimpleName;

import java.io.File;
import java.util.List;
import java.util.ArrayList;
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
            String packageName = cu.getPackageDeclaration().map(p -> p.getNameAsString()).orElse("");

            List<ClassOrInterfaceDeclaration> classes = cu.findAll(ClassOrInterfaceDeclaration.class);

            if(classes.isEmpty()){
                return Optional.empty();
            }
            List<ClassInfo> classInfoList = new ArrayList<>();

            for(ClassOrInterfaceDeclaration clazz : classes){ 
                String className = clazz.getNameAsString();
                String modifier = getModifier(clazz.getModifiers());
                List<MethodSig> methods = clazz.getMethods().stream().map(FileParser::toMethodSig).collect(Collectors.toList());

                classInfoList.add(new ClassInfo(className, modifier, methods));


            }

            List<String> imports = cu.getImports().stream().map(imp -> imp.getNameAsString()).collect(Collectors.toList());

            // use first class name for compatibility with dot generator 
            String firstClassName = classInfoList.get(0).className;

            return Optional.of(new FileInfo(file.getPath(), firstClassName, packageName, classInfoList.get(0).methods, imports, classInfoList));


        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private static MethodSig toMethodSig(MethodDeclaration method){

        String params = method.getParameters().stream().map(p -> p.getType().toString()).collect(Collectors.joining(", "));

        String returnType = method.getType().toString();

        String modifier = getModifier(method.getModifiers());

        return new MethodSig(method.getNameAsString(), params, returnType, modifier);


    }


    private static String getModifier(com.github.javaparser.ast.NodeList<com.github.javaparser.ast.Modifier> modifiers){
        if(modifiers.contains(com.github.javaparser.ast.Modifier.publicModifier())){
            return "public";
        } else if(modifiers.contains(com.github.javaparser.ast.Modifier.protectedModifier())){
            return "protected";
        } else if(modifiers.contains(com.github.javaparser.ast.Modifier.privateModifier())){
            return "private";
        } else {
            return "package-private";
        }

    }


}

