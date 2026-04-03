package com.codetriage.model;

public class MethodSig {
    public final String name;
    public final String params;
    public final String returnType;


    public MethodSig(String name, String params, String returnType){

        this.name = name;
        this.params = params;
        this.returnType = returnType;

    }

    public String signature() {
        String type = returnType == null || returnType.isEmpty() ? "void" : returnType;
        return String.format("%s(%s) -> %s", name, params , type);
    }
}