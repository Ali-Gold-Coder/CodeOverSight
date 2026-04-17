package com.codetriage.render;

import com.codetriage.model.FileInfo;
import com.google.gson.Gson;
import com.codetriage.model.TreeNode;
import com.codetriage.util.TreeBuilder;

import java.io.File;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class HtmlRenderer {
    private static final Gson gson = new Gson();


    public static void render (String dot, List<FileInfo> fileInfos, List<File> sourceFiles, String rootPath, String outputPath) throws Exception{

        // reading templates
        String dependencyTemplate = readTemplate("template.html");
        String treeTemplate = readTemplate("template-tree.html");


        String fileInfoJson = gson.toJson(fileInfos);

        // generate dependency report 
        String dependencyHtml = dependencyTemplate.replace("{{DOT}}", dot).replace("{{FILE_INFO_JSON}}", fileInfoJson);

        // Generate tree report
        TreeNode root = TreeBuilder.buildTree(sourceFiles, fileInfos, rootPath);
        String treeHtml = TreeRenderer.render(root);
        String treeReport = treeTemplate.replace("{{TREE_VIEW}}", treeHtml);

        // Write both files
        Files.write(Paths.get(outputPath), dependencyHtml.getBytes());

        //Generate tree filenames from output path 
        String treeOutputPath = insertBeforeExtension(outputPath, "-tree");
        Files.write(Paths.get(treeOutputPath), treeReport.getBytes());
        

        System.out.println("Generated reports:");
        System.out.println("  - " + outputPath + " (Dependency Graph)");
        System.out.println("  - " + treeOutputPath + " (File Structure Tree)");


    }

    private static String readTemplate(String templateName) throws Exception {
        InputStream templateStream = HtmlRenderer.class.getClassLoader().getResourceAsStream(templateName);
        if(templateStream == null){
            throw new RuntimeException(templateName + " not found in resources");
        }
        return new String(templateStream.readAllBytes(), StandardCharsets.UTF_8);
    }

    private static String insertBeforeExtension(String path, String insert) {
        int lastDot = path.lastIndexOf('.');
        if(lastDot == -1) {
            return path + insert;
        }
        return path.substring(0, lastDot) + insert + path.substring(lastDot);
    }
}