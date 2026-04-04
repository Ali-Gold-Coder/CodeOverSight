package com.codetriage.cli;
import org.apache.commons.cli.*;

import java.util.Arrays;
import java.util.List;

public class ArgsParser {
    public static Config parse(String[] args) throws Exception{

        Options options = new Options();

        options.addOption(Option.builder().longOpt("folders").hasArg().required().desc("comma-seperate folder paths").build());

        options.addOption(Option.builder().longOpt("depth").hasArg().desc("Max depth to follow imports (Default: 2").build());

        options.addOption(Option.builder().longOpt("limit").hasArg().desc("Max files to parse (default: 100").build());

        options.addOption(Option.builder().longOpt("include").hasArg().desc("File pattern to include (default: *.java").build());

        options.addOption(Option.builder().longOpt("exclude").hasArg().desc("File pattern to exclude (default: *Test.java").build());

        options.addOption(Option.builder().longOpt("output").hasArg().desc("Output HTML path (default: ./report.html").build());

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        Config config = new Config();

        config.folders = Arrays.asList(cmd.getOptionValue("folders").split(","));
        if(cmd.hasOption("depth")){
            config.depth = Integer.parseInt(cmd.getOptionValue("depth"));
        }

        if(cmd.hasOption("limit")){
            config.limit = Integer.parseInt(cmd.getOptionValue("limit"));
        }
        if(cmd.hasOption("include")){
            config.include = cmd.getOptionValue("include");
        }
        if(cmd.hasOption("exclude")){
            config.exclude = cmd.getOptionValue("exclude");
        }

        if(cmd.hasOption("output")){
            config.output = cmd.getOptionValue("output");
        }

        if( config.depth < 0 || config.depth > 3){
            throw new IllegalArgumentException("Depth must be between 0 and 3");
        }

        if ( config.limit < 1 || config.limit > 100){
            throw new IllegalArgumentException("Limit must be between 1 and 100");
        }

        return config;

    }

}
