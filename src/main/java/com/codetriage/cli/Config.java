package com.codetriage.cli;

import java.util.List;

public class Config {
    public List<String> folders;
    public int depth = 2;
    public int limit = 100;
    public String include = "*.java";
    public String exclude ="*Test.java";
    public String output = "./report.html";


}
