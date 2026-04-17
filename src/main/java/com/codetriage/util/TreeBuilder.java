package com.codetriage.util;

import com.codetriage.model.ClassInfo;
import com.codetriage.model.FileInfo;
import com.codetriage.model.MethodSig;
import com.codetriage.model.TreeNode;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class TreeBuilder{

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


            // Create file node (green square)

            TreeNode fileNode = new TreeNode(file.getName(), "FILE");
            fileNode.setFullPath(filePath);
            currentFolder.addChild(fileNode);

            // Add classes from this file (if parsed)
            if(fileInfo != null){
                for(ClassInfo classInfo : fileInfo.classes){
                    TreeNode classNode = new TreeNode(classInfo.className, "CLASS", classInfo.modifier);
                    fileNode.addChild(classNode);

                    // add methods

                    for(MethodSig method : classInfo.methods){

                        TreeNode methodNode = new TreeNode(method.name, "METHOD", method.modifier);

                        methodNode.setSignature(method.signature());
                        classNode.addChild(methodNode);
                    } // error maybe , dont have a var named methods 


                    // add imports
                    if(!fileInfo.imports.isEmpty()){
                        for(String importStmt : fileInfo.imports){
                            TreeNode importNode = new TreeNode(importStmt, "IMPORT");
                            classNode.addChild(importNode);
                        } // same issue here imports isnt a var
                    }
                }


            }
        }

        return root;

    }
}