package com.codetriage.model;

import java.util.List;

public class FileInfo {
    public final String path;
    public final String className;
    public final String packageName;
    public final List<MethodSig> methods;
    public final List<String> imports;

    public FileInfo(String path, String className, String packageName, List<MethodSig> methods, List<String> imports ){

        this.path = path;
        this.className = className;
        this.packageName = packageName;
        this.methods = methods;
        this.imports = imports;
    }

}
