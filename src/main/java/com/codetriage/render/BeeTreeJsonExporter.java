package com.codetriage.render;

import com.codetriage.model.TreeNode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class BeeTreeJsonExporter {

    public static String exportBeeTree(TreeNode root) {
        StringBuilder json = new StringBuilder();
        
        json.append("{\n");
        json.append("  \"metadata\": {\n");
        json.append("    \"generated\": \"").append(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME)).append("\",\n");
        json.append("    \"projectName\": \"CodeOverSight\"\n");
        json.append("  },\n");
        json.append("  \"tree\": ");
        json.append(nodeToJson(root, 2));
        json.append("\n}\n");
        
        return json.toString();
    }

    private static String nodeToJson(TreeNode node, int indent) {
        StringBuilder json = new StringBuilder();
        String indentStr = getIndent(indent);
        String nextIndentStr = getIndent(indent + 2);
        
        json.append("{\n");
        json.append(nextIndentStr).append("\"name\": \"").append(escapeJson(node.name)).append("\",\n");
        json.append(nextIndentStr).append("\"type\": \"").append(node.type).append("\"");
        
        if (node.description != null && !node.description.isEmpty()) {
            json.append(",\n");
            json.append(nextIndentStr).append("\"description\": ").append(escapeJsonString(node.description));
        }
        
        if (!node.children.isEmpty()) {
            json.append(",\n");
            json.append(nextIndentStr).append("\"children\": [\n");
            
            for (int i = 0; i < node.children.size(); i++) {
                TreeNode child = node.children.get(i);
                json.append(getIndent(indent + 4));
                json.append(nodeToJson(child, indent + 4));
                
                if (i < node.children.size() - 1) {
                    json.append(",");
                }
                json.append("\n");
            }
            
            json.append(nextIndentStr).append("]");
        }
        
        json.append("\n");
        json.append(indentStr).append("}");
        
        return json.toString();
    }

    private static String getIndent(int spaces) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < spaces; i++) {
            sb.append(" ");
        }
        return sb.toString();
    }

    private static String escapeJson(String str) {
        if (str == null) return "";
        return str.replace("\\", "\\\\")
                  .replace("\"", "\\\"")
                  .replace("\n", "\\n")
                  .replace("\r", "\\r")
                  .replace("\t", "\\t");
    }

    private static String escapeJsonString(String str) {
        if (str == null) return "\"\"";
        String escaped = escapeJson(str);
        return "\"" + escaped + "\"";
    }
}