package com.codetriage.render;

import com.codetriage.model.TreeNode;

public class TreeRenderer {

    public static String render( TreeNode root){

        StringBuilder html = new StringBuilder();

        html.append("<div id=\"tree-container\" style=\"padding: 16px;\">\n");
        html.append("<h4 style=\"margin-top: 0;\">📁 Code Structure Tree</h4>\n");
        html.append("<div id=\"tree-view\" style=\"font-family: 'Monaco', monospace; font-size: 13px;\">\n");

        renderNode(root, 0, html);

        html.append("</div>\n");
        html.append("</div>\n");
        html.append(getTreeCSS());
        html.append(getTreeJS());


        return html.toString();

    }

    private static void renderNode ( TreeNode node, int depth, StringBuilder html){

        if(node.type.equals("FOLDER") && node.name.equals("root")){

            //skip the root and render children directly

            for(TreeNode child : node.children){
                renderNode(child, depth, html);
            }

            return;
        }


        String indent = getIndent(depth);
        String icon = getIcon(node.type);
        String color = getColor(node.type);
        String className = getNodeClass(node.type);

        html.append(String.format("%s<div class='tree-node %s' style='color: %s; margin-left: %dpx;'>\n", indent, className, color, depth * 16));

        if(node.children.isEmpty()){
            html.append(String.format("%s  <span class='tree-icon'>%s</span> %s\n", indent, icon, escapeHtml(node.name)));

            if(!node.modifier.isEmpty() && !node.type.equals("IMPORT")){
                html.append(String.format(" <span class='modifier-badge'>%s</span>", node.modifier));

            }

            if(node.signature != null && !node.signature.isEmpty()){

                html.append(String.format(" <span class='method-signature'>%s</span>", escapeHtml(node.signature)));

            }

            html.append("\n");

        } else{ 

            // Has children, make it collapsible
            html.append(String.format("%s  <details class='tree-details'>\n", indent));
            html.append(String.format("%s    <summary class='tree-summary'>\n", indent));
            html.append(String.format("%s      <span class='tree-icon'>%s</span> %s\n", indent, icon, escapeHtml(node.name)));

            if(!node.modifier.isEmpty()){
                html.append(String.format(" <span class='modifier-badge'>%s</span>", node.modifier));
            }
            html.append(String.format("%s    </summary>\n", indent));

            for(TreeNode child : node.children){
                renderNode(child, depth + 1, html);
            }

            html.append(String.format("%s  </details>\n", indent));

        }

        html.append(String.format("%s</div>\n", indent));
    }

    private static String getIcon(String type){

        switch(type) {

            case "FOLDER":
                return "📁";

            case "FILE":
                return "📄";

            case "CLASS":
                return "🏛️";

            case "METHOD":
                return "⚙️";
            
            case "IMPORT":
                return "📦";

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
            return "#666";
        case "IMPORT":
            return "#999";
        default:
            return "#000";
        }

    }

}