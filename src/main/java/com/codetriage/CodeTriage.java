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
            // parse CLI argument
            Config config = ArgsParser.parse(args);


            // List fiels with respect to limits
            List<File> files = FileLister.listFiles(config);

            if(files.isEmpty()){
                System.err.println("No files found matching criteria");
                System.exit(1);
            }

            if(files.size() >= config.limit){
                System.err.printf("Warning: Selected folders contain %d files. Parsing first %d.%n ", files.size(), config.limit);

            }

            // parse files with timeout handling
            List<FileInfo> fileInfos = files.stream().limit(config.limit).map(file -> {
                Optional<FileInfo> info = FileParser.parse(file, config);
                 if( info.isEmpty()){
                    System.err.println("Warning: Failed to parse "+ file.getName());
                 }
                 return info;
            }).filter(Optional::isPresent).map(Optional::get).collect(Collectors.toList());

            // Generate DOT graph
            String dot = DotGenerator.generate(fileInfos, config);

            //Render HTML report
            String html = HtmlRenderer.render(dot, fileInfos);
            Files.write(Paths.get(config.output), html.getBytes());

            // auto open browser

            if( Desktop.isDesktopSupported() ){
                Desktop.getDesktop().open(new File(config.output));
            }

            System.out.println("Report generated: " + config.output);

        }catch (Exception e){
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}