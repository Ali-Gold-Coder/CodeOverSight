package com.codetriage.render;

import com.codetriage.model.FileInfo;
import com.google.gson.Gson;
import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class HtmlRenderer {
    private static final Gson gson = new Gson();


    public static String render (String dot, List<FileInfo> fileInfos) throws Exception{

        InputStream templateStream = HtmlRenderer.class.getClassLoader().getResourceAsStream("template.html");

        if(templateStream == null){
            throw new RuntimeException("template.html not found in resources");
        }

        String template = IOUtils.toString(templateStream, StandardCharsets.UTF_8);
        String fileInfoJson = gson.toJson(fileInfos);

        return template.replace("{{DOT}}", dot).replace("{{FILE_INFO_JSON}}", fileInfoJson);

    }
}