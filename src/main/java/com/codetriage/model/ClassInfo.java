package com.codetriage.model;

import java.util.List;


public class ClassInfo{

    public final String className;
    public final String modifier; // public , private , etc 
    public final List<MethodSig> methods;

    public ClassInfo(String className, String modifier, List<MethodSig> methods){

        this.className = className;
        this.modifier = modifier;
        this.methods = methods; 
    }
}