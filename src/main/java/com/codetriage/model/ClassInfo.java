package com.codetriage.model;

import java.util.List;


public class ClassInfo{

    public final String className;
    public final String modifier; // public , private , etc 
    public final List<MethodSig> methods;
    public final List<String> extensions;
    public final List<String> interfaces;
    public final List<String> exceptions;


    public ClassInfo(String className, String modifier, List<MethodSig> methods, List<String> exceptions, List<String> interfaces, List<String> extensions){

        this.className = className;
        this.modifier = modifier;
        this.methods = methods; 
        this.exceptions = exceptions;
        this.interfaces = interfaces;
        this.extensions = extensions;
    }
}