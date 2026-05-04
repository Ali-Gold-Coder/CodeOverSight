package com.codetriage.render;

import com.codetriage.model.TreeNode;

public class BeeTreeRenderer {

    public static String render(TreeNode root){
        String dot = renderDot(root);
        
        // Wrap DOT in HTML with d3-graphviz rendering
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>\n");
        html.append("<html lang=\"en\">\n");
        html.append("<head>\n");
        html.append("    <meta charset=\"UTF-8\">\n");
        html.append("    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n");
        html.append("    <title>CodeTriage Report - Bee Tree Structure</title>\n");
        html.append("    <script src=\"https://d3js.org/d3.v7.min.js\"></script>\n");
        html.append("    <script src=\"https://unpkg.com/d3-graphviz@5.0.2/build/d3-graphviz.min.js\"></script>\n");
        html.append("    <style>\n");
        html.append("        * { box-sizing: border-box; }\n");
        html.append("        body { \n");
        html.append("            margin: 0; \n");
        html.append("            font-family: -apple-system, BlinkMacSystemFont, \"Segoe UI\", Roboto, sans-serif; \n");
        html.append("            background: #fafafa; \n");
        html.append("        }\n");
        html.append("        #graph { \n");
        html.append("            width: 100%; \n");
        html.append("            height: 100vh; \n");
        html.append("            background: white; \n");
        html.append("        }\n");
        html.append("    </style>\n");
        html.append("</head>\n");
        html.append("<body>\n");
        html.append("<div id=\"graph\"></div>\n");
        html.append("<script>\n");
        html.append("    const treeDot = `").append(escapeBackticks(dot)).append("`;\n");
        html.append("    d3.select(\"#graph\").graphviz()\n");
        html.append("        .fit(true)\n");
        html.append("        .zoom(true)\n");
        html.append("        .renderDot(treeDot);\n");
        html.append("</script>\n");
        html.append("</body>\n");
        html.append("</html>\n");
        
        return html.toString();
    }

    private static String renderDot(TreeNode root){
        StringBuilder dot = new StringBuilder();

        dot.append("digraph BeeTree {\n");
        dot.append("  rankdir=TB;\n");
        dot.append("  splines=ortho;\n");
        dot.append("  nodesep=1;\n");
        dot.append("  ranksep=2;\n");
        dot.append("  node [shape=box, style=\"rounded,filled\", fontname=\"Arial\", fontsize=10, labeljust=l];\n");
        dot.append("  edge [color=\"#666\", penwidth=1];\n\n");

        // Recursively render nodes
        renderNode(root, dot);

        dot.append("}\n");
        return dot.toString();
    }

    private static String escapeBackticks(String text) {
        if (text == null) {
            return "";
        }
        return text.replace("`", "\\`");
    }

    private static void renderNode(TreeNode node, StringBuilder dot){
        // Only render nodes with valid types
        if (!isValidType(node.type)) {
            return;
        }

        String nodeId = sanitizeId(node.name);
        String color = getColor(node.type);
        String label = node.name;
        
        // For files, include description in label
        if (node.type.equals("FILE") && node.description != null && !node.description.isEmpty()) {
            label = node.name + "\\n\\n" + escapeLabel(node.description);
        }

        // Create node
        dot.append(String.format("  \"%s\" [label=\"%s\", fillcolor=\"%s\"];\n", nodeId, label, color));

        // Recursively render children and create edges
        for (TreeNode child : node.children) {
            if (isValidType(child.type)) {
                String childId = sanitizeId(child.name);
                dot.append(String.format("  \"%s\" -> \"%s\";\n", nodeId, childId));
                renderNode(child, dot);
            }
        }
    }

    private static String sanitizeId(String name) {
        return name.replaceAll("[^a-zA-Z0-9_]", "_");
    }

    private static boolean isValidType(String type) {
        return type != null && ( type.equals("FOLDER") || type.equals("FILE") );
    }

    private static String getColor(String type) {
        switch(type) {
            case "FOLDER":
                return "#2196F3";
            case "FILE":
                return "#4CAF50";
            default:
                return "#000";
        }
    }

    private static String escapeLabel(String text) {
        if (text == null) {
            return "";
        }
        // Escape special characters for Graphviz labels
        return text.replace("\\", "\\\\")
                   .replace("\"", "\\\"")
                   .replace("\n", "\\n")
                   .replace("\r", "");
    }
}