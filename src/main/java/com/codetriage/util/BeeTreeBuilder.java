package com.codetriage.util;

import com.codetriage.model.ClassInfo;
import com.codetriage.model.FileInfo;
import com.codetriage.model.MethodSig;
import com.codetriage.model.TreeNode;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class BeeTreeBuilder{

    public static TreeNode buildTree(List<File> sourceFiles, List<FileInfo> parsedFiles, String rootPath){

        TreeNode root = new TreeNode("root", "FOLDER");
        root.setFullPath(rootPath);


        // Create a map of file paths to fileinfo for quick lookup

        Map<String, FileInfo> fileInfoMap = new HashMap<>();

        for(FileInfo info : parsedFiles){
            fileInfoMap.put(info.path, info);
        }

        // Create folder structure for source files

        Map<String, TreeNode> folderCache = new HashMap<>();
        folderCache.put(rootPath, root);

        for(File file : sourceFiles){
            String filePath = file.getAbsolutePath();
            FileInfo fileInfo = fileInfoMap.get(filePath);


            // Create folder hierarchy
            File parentFile = file.getParentFile();
            String parentPath = rootPath;
            List<String> folderChain = new ArrayList<>();

            while(parentFile != null && !parentFile.getAbsolutePath().equals(rootPath)){

                folderChain.add(0, parentFile.getName());
                parentFile = parentFile.getParentFile();

            }

            TreeNode currentFolder = root;
            String currentPath = rootPath;

            for( String folderName : folderChain){
                currentPath = currentPath + File.separator + folderName;

                final TreeNode parentFolder = currentFolder;
                
                TreeNode folderNode = folderCache.computeIfAbsent(currentPath, k -> {

                    TreeNode folder = new TreeNode(folderName, "FOLDER");
                    folder.setFullPath(k);
                    parentFolder.addChild(folder);
                    return folder;

                } );
                currentFolder = folderNode;
            }


            // Create file with description
            String fileDescription = generateFileDescription(fileInfo);

            TreeNode fileNode = new TreeNode(file.getName(), "FILE");
            fileNode.setFullPath(filePath);
            fileNode.setDescription(fileDescription);
            currentFolder.addChild(fileNode);



           
        }

        return root;

    }

    private static String generateFileDescription(FileInfo fileInfo){

        if(fileInfo == null){
            return "";
        }

        StringBuilder desc = new StringBuilder();

        // add classes description
        if(fileInfo.classes != null && !fileInfo.classes.isEmpty()) {
            desc.append("Classes:\n");

            for(ClassInfo classInfo : fileInfo.classes){
                String extensions = classInfo.extensions != null && !classInfo.extensions.isEmpty() 
                    ? " | extends " + String.join(", ", classInfo.extensions) 
                    : "";
                desc.append("  ").append(classInfo.modifier).append(" ").append(classInfo.className).append(extensions).append("\n");

                // add constructors and methods for this class
                if(classInfo.methods != null && !classInfo.methods.isEmpty()) {
                    // desc.append("    Constructors:\n");

                    Optional<MethodSig> constructor = classInfo.methods.stream().filter(m -> m.name.equals(classInfo.className)).findFirst();

                    if( constructor.isPresent()){
                        String params = constructor.get().params != null ? constructor.get().params : "";
                        desc.append("   Constructor parameters: ").append(params).append("\n");
                    }
                    
                    // for(MethodSig method : classInfo.methods){
                    //     if(method.name.equals(classInfo.className)){ // Constructor has same name as class
                    //         String params = method.params != null ? method.params : "";
                    //         desc.append("      ").append(method.modifier).append(" | (").append(params).append(")\n");
                    //     }
                    // }

                    desc.append("    Methods / Functions:\n");
                    for(MethodSig method : classInfo.methods){
                        if(!method.name.equals(classInfo.className)){ // Skip constructors
                            String params = method.params != null ? method.params : "";
                            String returnType = method.returnType != null ? method.returnType : "void";
                            String methodExceptions = method.exceptions != null && !method.exceptions.isEmpty() 
                                ? " throws " + String.join(", ", method.exceptions) 
                                : "";

                            desc.append("      ").append(method.modifier).append(" ").append(returnType).append(" ")
                                .append(method.name).append(" | (").append(params).append(")").append(methodExceptions)
                                .append(" -> ").append(returnType).append("\n");

                            // desc.append("      ").append(method.modifier).append(" ").append(returnType).append(" ")
                            //     .append(method.name).append(" | (").append(params).append(")").append(methodExceptions).append("\n");
                        }
                    }
                }
            }
        }

        return desc.toString().trim();
    }
}