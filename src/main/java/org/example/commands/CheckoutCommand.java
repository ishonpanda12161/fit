package org.example.commands;

import org.example.core.ObjectStore;

import java.io.File;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class CheckoutCommand implements Command {

    @Override
    public void execute(String[] args) {

        if (args.length != 1) {
            System.out.println("Usage: fit checkout <commitHash>");
            return;
        }

        String commitHash = args[0];
        checkoutCommit(commitHash);
    }

    public void checkoutCommit(String commitHash) {
        try {
            checkoutToWorktree(commitHash);

            Files.writeString(new File(".fit/HEAD").toPath(),
                    "detached: " + commitHash);

            System.out.println("Checked out commit " + commitHash);

        } catch (Exception e) {
            System.out.println("Checkout failed.");
            e.printStackTrace();
        }
    }

    public void checkoutToWorktree(String commitHash) throws Exception {

        if (commitHash == null || commitHash.isBlank()) {
            System.out.println("Invalid branch: no commit recorded.");
            return;
        }

        File commitObj = new File(".fit/objects/" + commitHash);

        if (!commitObj.exists() || commitObj.isDirectory()) {
            System.out.println("Commit not found: " + commitHash);
            return;
        }

        String commitData = Files.readString(commitObj.toPath());
        String treeHash = extractTree(commitData);

        if (treeHash == null || treeHash.isBlank()) {
            System.out.println("Invalid commit: missing tree hash.");
            return;
        }

        File treeObj = new File(".fit/objects/" + treeHash);

        if (!treeObj.exists() || treeObj.isDirectory()) {
            System.out.println("Tree object missing: " + treeHash);
            return;
        }

        String treeData = Files.readString(treeObj.toPath());
        Map<String, String> files = parseTree(treeData);

        for (Map.Entry<String, String> entry : files.entrySet()) {
            String filename = entry.getKey();
            String blobHash = entry.getValue();

            byte[] content = ObjectStore.read(blobHash);

            File out = new File(filename);
            if (out.getParentFile() != null)
                out.getParentFile().mkdirs();

            Files.write(out.toPath(), content);

            System.out.println("Restored: " + filename);
        }
    }

    private String extractTree(String commitData) {
        for (String line : commitData.split("\n")) {
            if (line.startsWith("tree")) {
                return line.substring(5).trim();
            }
        }
        return null;
    }

    private Map<String, String> parseTree(String data) {
        Map<String, String> map = new HashMap<>();

        for (String line : data.split("\n")) {
            if (line.isBlank()) continue;
            String[] p = line.split(" ");
            if (p.length == 2) {
                map.put(p[0], p[1]);
            }
        }

        return map;
    }
}
