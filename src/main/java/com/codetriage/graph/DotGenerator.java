package com.codetriage.graph;

import com.codetriage.cli.Config;
import com.codetriage.model.FileInfo;
import com.codetriage.model.MethodSig;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class DotGenerator {

    public static String generate(List<FileInfo> files, Config config){

        Set<String> internalClasses = files.stream().map(f -> f.packageName + "." +f.className).collect(Collectors.toSet());

        StringBuilder dot = new StringBuilder();

        dot.append("digraph CideTriage {\n");
        dot.append("  rankdir=LR;\n");
        dot.append("  node [shape=box, style=rounded];\n");
        dot.append("  edge [color=blue];\n\n");


        // Nodes
        for (FileInfo file : files){

            String nodeId = getNodeId(file);
            String label = buildNodeLabel(file);
            dot.append(String.format("  \\\"%s\\\" [label=\\\"%s\\\"];%n", nodeId, label));


        }

        // Edges

        for(FileInfo file : files){
            String formId = getNodeId(file);

            for(String importName : file.imports){
                String toId = resolveImport(importName, internalClasses, files);

                if(toId != null){
                    String color = internalClasses.contains(importName) ? "blue" : "gray";
                    dot.append(String.format("  \\\"%s\\\" -> \\\"%s\\\" [color=%s];%n", formId, toId, color));
                }
            }
        }

        dot.append("}");
        return dot.toString();
    }

    private static String getNodeId(FileInfo file) {
        return file.packageName.isEmpty() ? file.className : file.packageName + "." + file.className;

    }

    private static String buildNodeLabel(FileInfo file){
        StringBuilder label = new StringBuilder(file.className + "|");

        for(MethodSig method : file.methods){
            label.append(method.signature()).append("\\n");
        }

        return label.toString().replace("\"", "\\\"");
    }

    private static String resolveImport(String importName, Set<String> internalClasses, List<FileInfo> files ){

        if(internalClasses.contains(importName)){
            return importName;
        }

        // Try to find by simple name
        for(FileInfo file : files){
            if(importName.endsWith("." + file.className)){
                return getNodeId(file);
            }
        }

        return null;
    }
}