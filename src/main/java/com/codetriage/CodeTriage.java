package com.codetriage;

import com.codetriage.cli.ArgsParser;
import com.codetriage.cli.Config;
import com.codetriage.graph.DotGenerator;
import com.codetriage.model.FileInfo;
import com.codetriage.parser.FileParser;
import com.codetriage.render.HtmlRenderer;
import com.codetriage.util.FileLister;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CodeTriage {
    public static void main(String[] args){

        try{

        }catch (Exception e){
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}