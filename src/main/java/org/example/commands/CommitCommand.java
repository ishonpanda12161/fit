package org.example.commands;

import org.example.core.Index;
import org.example.core.ObjectStore;

import java.io.File;
import java.nio.file.Files;
import java.util.Map;

public class CommitCommand implements Command {

    @Override
    public void execute(String[] args) {

        if (args.length < 2 || !args[0].equals("-m")) {
            System.out.println("Usage: fit commit -m \"message\"");
            return;
        }

        String message = args[1];

        try {

            Map<String, String> index = Index.load();

            if (index.isEmpty()) {
                System.out.println("Nothing to commit.");
                return;
            }

            String treeData = buildTreeData(index);
            String treeHash = ObjectStore.store(treeData.getBytes());

            String parent = readMasterCommit();
            String commitData = buildCommitObject(treeHash, message, parent);
            String commitHash = ObjectStore.store(commitData.getBytes());

            updateMaster(commitHash);

            // IMPORTANT: clear index *after* writing commit
            Index.save(new java.util.HashMap<>());

            System.out.println("Committed: " + commitHash);

        } catch (Exception e) {
            System.out.println("Commit failed.");
            e.printStackTrace();
        }
    }

    private String buildTreeData(Map<String, String> index) {
        StringBuilder sb = new StringBuilder();
        for (var entry : index.entrySet()) {
            sb.append(entry.getKey()).append(" ").append(entry.getValue()).append("\n");
        }
        return sb.toString();
    }

    private String buildCommitObject(String treeHash, String msg, String parent) {
        return "tree " + treeHash + "\n" +
                "parent " + parent + "\n" +
                "message " + msg + "\n" +
                "timestamp " + System.currentTimeMillis() + "\n";
    }

    private String readMasterCommit() {
        try {
            File master = new File(".fit/refs/master");
            if (!master.exists() || master.length() == 0) return "null";
            return Files.readString(master.toPath()).trim();
        } catch (Exception e) {
            return "null";
        }
    }

    private void updateMaster(String hash) throws Exception {
        Files.writeString(new File(".fit/refs/master").toPath(), hash);
    }
}
