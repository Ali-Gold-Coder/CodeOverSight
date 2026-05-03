package com.codetriage.render;

import com.codetriage.model.TreeNode;

public class BeeTreeRenderer {

    public static String render( TreeNode root){

        StringBuilder html = new StringBuilder();

        html.append("<!DOCTYPE html>\n");
        html.append("<html>\n");
        html.append("<head>\n");
        html.append("  <title>Bee Tree Report</title>\n");
        html.append(getTreeCSS());
        html.append("</head>\n");
        html.append("<body>\n");
        html.append("<div id='tree-container'>\n");
        html.append("<h1>Code Structure - Bee Tree</h1>\n");

        renderNode(root, html, 0);

        html.append("</div>\n");
        html.append(getTreeJS());
        html.append("</body>\n");
        html.append("</html>\n");

        return html.toString();

    }

    private static void renderNode(TreeNode node, StringBuilder html, int depth){
        if (!isValidType(node.type)) {
            return;
        }

        String indent = getIndent(depth);
        String icon = getIcon(node.type);

        if(node.type.equals("FOLDER")){
            html.append(indent).append("<details class='tree-node tree-folder'>\n");
            html.append(indent).append("  <summary class='tree-summary'>").append(icon).append(" ").append(escapeHtml(node.name)).append("</summary>\n");

            for (TreeNode child : node.children){
                if (isValidType(child.type)){
                    renderNode(child, html, depth + 1);
                }
            }
            html.append(indent).append("</details>\n");
            
        } 
        
        else if (node.type.equals("FILE")){

            html.append(indent).append("<details class='tree-node tree-file'>\n");
            html.append(indent).append("  <summary class='tree-summary'>").append(icon).append(" ").append(escapeHtml(node.name)).append("</summary>\n");
            
            // Add file description if it exists
            if (node.description != null && !node.description.isEmpty()) {
                html.append(indent).append("  <div class='file-description'>\n");
                html.append(indent).append("    <pre>").append(escapeHtml(node.description)).append("</pre>\n");
                html.append(indent).append("  </div>\n");
            }
            
            html.append(indent).append("</details>\n");
        }
        
    }

    private static String sanitizeId(String name) {
        return name.replaceAll("[^a-zA-Z0-9_]", "_");
    }

    private static boolean isValidType(String type) {
        return type != null && ( type.equals("FOLDER") || type.equals("FILE") );
    }
    
    private static String getIcon(String type){

        switch(type) {

            case "FOLDER":
                return "📁";

            case "FILE":
                return "📄";

            default:
                return "*";
        }
    }


    private static String getNodeClass(String type){

        return "tree-" + type.toLowerCase();
    }

    private static String getIndent(int depth){
        return "";
    }

    private static String escapeHtml(String text){

        if (text == null){
            return "";
        }

        return text.replace("&", "&amp;")
                   .replace("<", "&lt;")
                   .replace(">", "&gt;")
                   .replace("\"", "&quot;")
                   .replace("'", "&#39;");
    }

    private static String getTreeCSS(){

        return "<style>\n" +
            "#tree-container { background: white; border-top: 2px solid #ddd; margin-top: 20px; }\n" +
            ".tree-node { margin: 4px 0; padding: 4px; border-radius: 4px; transition: 0.2s; }\n" +
            ".tree-node:hover { background: #f5f5f5; padding-left: 8px; }\n" +
            ".tree-icon { margin-right: 6px; display: inline-block; width: 16px; }\n" +
            ".tree-details { cursor: pointer; user-select: none; }\n" +
            ".tree-details[open] > .tree-summary .tree-icon::before { content: '▼ '; }\n" +
            ".tree-details:not([open]) > .tree-summary .tree-icon::before { content: '▶ '; }\n" +
            ".tree-summary { list-style: none; cursor: pointer; padding: 4px 0; font-weight: 500; }\n" +
            ".tree-summary::-webkit-details-marker { display: none; }\n" +
            ".modifier-badge { display: inline-block; padding: 2px 6px; background: #f3f3f3; " +
            "border-radius: 3px; font-size: 11px; font-weight: 600; color: #666; margin-left: 6px; }\n" +
            ".method-signature { display: inline-block; padding: 2px 6px; background: #e3f2fd; " +
            "border-radius: 3px; font-size: 11px; color: #1565c0; margin-left: 6px; }\n" +
            ".tree-folder { font-weight: 600; }\n" +
            ".tree-file { font-weight: 500; }\n" +
            ".tree-class { font-weight: 600; }\n" +
            ".tree-method { font-style: italic; }\n" +
            ".tree-import { color: #999; font-size: 12px; }\n" +
            "</style>\n";

    }

    private static String getTreeJS() {
        return "<script>\n" +
            "// Tree interactivity handled by native <details> elements\n" +
            "// No additional JS needed for basic functionality\n" +
            "</script>\n";
    }


    private static String getColor(String type){

        switch(type) {
        case "FOLDER":
            return "#2196F3";
        case "FILE":
            return "#4CAF50";
        case "CLASS":
            return "#FF9800";
        case "METHOD":
            return "#9C27B0";
        case "IMPORT":
            return "#999";
        default:
            return "#000";
        }

    }

}