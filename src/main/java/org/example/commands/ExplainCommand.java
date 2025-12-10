package org.example.commands;

import java.io.File;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class ExplainCommand implements Command {

    @Override
    public void execute(String[] args) {

        if (args.length != 1) {
            System.out.println("Usage: fit explain <commitHash>");
            return;
        }

        String hash = args[0];

        try {
            String diff = generateSummary(hash);
            String safe = diff.replace("\n", "\\n").replace("\"", "\\\"");
            String ai = ExplainAI.summarize(hash, safe);

            System.out.println(diff);
            System.out.println("AI Summary:\n" + ai);

        } catch (Exception e) {
            System.out.println("Explain failed.");
            e.printStackTrace();
        }
    }

    private String generateSummary(String commitHash) throws Exception {

        File commitObj = new File(".fit/objects/" + commitHash);
        if (!commitObj.exists()) return "Commit not found.";

        String commitData = Files.readString(commitObj.toPath());
        String treeHash = extract(commitData, "tree");
        String parentHash = extract(commitData, "parent");

        Map<String, String> current = loadTree(treeHash);
        Map<String, String> parent =
                parentHash.equals("null") ? new HashMap<>() : loadTree(parentHash);

        StringBuilder sb = new StringBuilder();
        sb.append("Changes in ").append(commitHash).append(":\n");

        for (String f : current.keySet()) {
            if (!parent.containsKey(f)) sb.append("Added: ").append(f).append("\n");
            else if (!current.get(f).equals(parent.get(f))) sb.append("Modified: ").append(f).append("\n");
        }

        for (String f : parent.keySet()) {
            if (!current.containsKey(f)) sb.append("Deleted: ").append(f).append("\n");
        }

        return sb.toString();
    }

    private String extract(String commit, String key) {
        for (String line : commit.split("\n")) {
            if (line.startsWith(key)) return line.substring(key.length() + 1).trim();
        }
        return "null";
    }

    private Map<String, String> loadTree(String hash) throws Exception {

        File treeObj = new File(".fit/objects/" + hash);
        if (!treeObj.exists()) return new HashMap<>();

        String tree = Files.readString(treeObj.toPath());
        Map<String, String> map = new HashMap<>();

        for (String line : tree.split("\n")) {
            if (line.isBlank()) continue;
            String[] p = line.split(" ");
            map.put(p[0], p[1]);
        }

        return map;
    }
}
