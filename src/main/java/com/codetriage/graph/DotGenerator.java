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
        dot.append("  rankdir=TB;\n");
        dot.append("  splines=ortho;\n");
        dot.append("  nodesep=0.5;\n");
        dot.append("  ranksep=1.2;\n");
        dot.append("  node [shape=box, style=\"rounded,filled\", fillcolor=\"#e8f4f8\", fontname=\"Arial\"];\n");
        dot.append("  edge [color=\"#666\", penwidth=1.5];\n\n");

        // Group by package
        Map<String, List<FileInfo>> byPackage = files.stream()
            .collect(Collectors.groupingBy(f -> f.packageName.isEmpty() ? "default" : f.packageName));

        // Create subgraph clusters for each package
        for (Map.Entry<String, List<FileInfo>> entry : byPackage.entrySet()) {
            String pkgName = entry.getKey();
            String escapedPkg = pkgName.replace(".", "_");
            List<FileInfo> pkgFiles = entry.getValue();

            dot.append(String.format("  subgraph cluster_%s {\n", escapedPkg));
            dot.append(String.format("    label=\"%s\";\n", pkgName));
            dot.append("    style=filled; fillcolor=\"#f0f0f0\"; color=\"#999\"; penwidth=2;\n");

            // Add nodes for this package
            for (FileInfo file : pkgFiles) {
                String nodeId = getNodeId(file);
                String label = buildNodeLabel(file);
                dot.append(String.format("    \\\"%s\\\" [label=\\\"%s\\\"];\n", nodeId, label));
            }
            dot.append("  }\n\n");
        }

        // Edges
        for(FileInfo file : files){
            String formId = getNodeId(file);

            for(String importName : file.imports){
                String toId = resolveImport(importName, internalClasses, files);

                if(toId != null){
                    String color = internalClasses.contains(importName) ? "#2196F3" : "#999";
                    dot.append(String.format("  \\\"%s\\\" -> \\\"%s\\\" [color=\"%s\"];\n", formId, toId, color));
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
        StringBuilder label = new StringBuilder();
        label.append("Class: ").append(file.className).append("|");

        if(file.methods.isEmpty()){
            label.append("(no methods)");
        } else {
            for(MethodSig method : file.methods){
                label.append("\\nMethod: ").append(method.signature());
            }
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