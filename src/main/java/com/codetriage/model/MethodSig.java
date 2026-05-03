package com.codetriage.model;

import java.util.List;

public class MethodSig {
    public final String name;
    public final String params;
    public final String returnType;
    public final String modifier; // New : list public , protected , private , package private
    public final List<String> exceptions;


    public MethodSig(String name, String params, String returnType, String modifier, List<String> exceptions){

        this.name = name;
        this.params = params;
        this.returnType = returnType;
        this.modifier = modifier;
        this.exceptions = exceptions;

    }

    public String signature() {
        return String.format("%s(%s) → %s", name, params , returnType);
    }
}