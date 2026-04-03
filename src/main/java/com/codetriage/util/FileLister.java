package com.codetriage.util;

import com.codetriage.cli.Config;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FileLister {

    public static List<File> listFiles(Config config) throws Exception {

        List<File> result = new ArrayList<>();
        WildcardFileFilter includeFilter = new WildcardFileFilter(config.include);
        WildcardFileFilter excludeFiller = new WildcardFileFilter(config.exclude);


        for (String folderPath : config.folders){
            File folder = new File(folderPath.trim());

            if(!folder.exists() || !folder.isDirectory()){
                throw new IllegalArgumentException("Folder not found: " + folderPath);
            }

            Collection<File> files = FileUtils.listFiles(folder, includeFilter, TrueFileFilter.INSTANCE);

            for( File file : files){

                if( !excludeFiller.accept(file)){
                    result.add(file);

                    if(result.size() >= config.limit){
                        return result;
                    }
                }

            }
        }

        return  result;

    }
}