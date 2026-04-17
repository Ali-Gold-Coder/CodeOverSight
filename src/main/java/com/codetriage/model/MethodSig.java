package com.codetriage.model;

public class MethodSig {
    public final String name;
    public final String params;
    public final String returnType;
    public final String modifier; // New : list public , protected , private , package private


    public MethodSig(String name, String params, String returnType, String modifier){

        this.name = name;
        this.params = params;
        this.returnType = returnType;
        this.modifier = modifier;

    }

    public String signature() {
        return String.format("%s(%s) → %s", name, params , returnType);
    }
}