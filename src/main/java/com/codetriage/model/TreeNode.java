package com.codetriage.model;

import java.util.ArrayList;
import java.util.List;

public class TreeNode{
    public String name;
    public String type; // Folder, file, class , method , import
    public String modifier; // public , protected, etc
    public String fullPath; // Folders/files
    public String signature; // For methods 
    public List<TreeNode> children;

    public TreeNode(String name, String type){
        this.name = name;
        this.type = type;
        this.modifier = "";
        this.children = new ArrayList<>();
    }


    public TreeNode(String name, String type, String modifier){
        this(name, type);
        this.modifier = modifier;
    }

    public void addChild(TreeNode child){

        this.children.add(child);
    }

    public void setFullPath(String path){
        this.fullPath = path;
    }

    public void setSignature(String sig){
        this.signature = sig;
    }
}