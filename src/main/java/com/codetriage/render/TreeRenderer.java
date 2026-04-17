package com.codetriage.render;

import com.codetriage.model.TreeNode;

public class TreeRenderer {

    public static String render( TreeNode root){

        StringBuilder dot = new StringBuilder();

        dot.append("digraph CodeStructure {\n");
        dot.append("  rankdir=TB;\n");
        dot.append("  splines=ortho;\n");
        dot.append("  nodesep=1;\n");
        dot.append("  ranksep=2;\n");
        dot.append("  node [shape=box, style=\"rounded,filled\", fontname=\"Arial\", fontsize=10];\n");
        dot.append("  edge [color=\"#666\", penwidth=1];\n\n");

        // Recursively render nodes
        renderNode(root, dot);


        dot.append("}\n");
        return dot.toString();

    }

    private static void renderNode ( TreeNode node, StringBuilder dot){

        String nodeId = sanitizeId(node.name);
        String label = node.name;
        String color = getColor(node.type);
        String icon = getIcon(node.type);

        // Create node
        dot.append(String.format("  \"%s\" [label=\"%s %s\", fillcolor=\"%s\"];\n", nodeId, icon, label, color));

        // Recursively render children and create edges
        for (TreeNode child : node.children) {
            String childId = sanitizeId(child.name);
            dot.append(String.format("  \"%s\" -> \"%s\";\n", nodeId, childId));
            renderNode(child, dot);
        }
    }

    private static String sanitizeId(String name) {
        return name.replaceAll("[^a-zA-Z0-9_]", "_");
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