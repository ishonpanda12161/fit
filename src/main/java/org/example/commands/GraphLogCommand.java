package org.example.commands;

import org.example.Utils.MergeUtil;

import java.io.File;
import java.nio.file.Files;

public class GraphLogCommand implements Command {

    @Override
    public void execute(String[] args) {

        try {
            String headRef = MergeUtil.currentBranchRef();
            String headBranch = headRef.replace("refs/", "");
            String currentCommit = MergeUtil.readRefCommit(headRef);

            if (currentCommit == null) {
                System.out.println("No commits yet.");
                return;
            }

            String commitHash = currentCommit;

            while (commitHash != null && !commitHash.equals("null")) {

                File obj = new File(".fit/objects/" + commitHash);
                if (!obj.exists()) break;

                String data = Files.readString(obj.toPath());
                printCommitNode(commitHash, data, currentCommit, headBranch);

                String[] parents = MergeUtil.extractParents(data);
                if (parents.length == 0) break;

                commitHash = parents[0];
            }

        } catch (Exception e) {
            System.out.println("Graph log failed.");
            e.printStackTrace();
        }
    }

    private void printCommitNode(String hash, String data, String headHash, String branchName) {
        String msg = extractMessage(data);

        String shortHash = hash.length() > 8 ? hash.substring(0, 8) : hash;

        boolean isHead = hash.equals(headHash);

        if (isHead) {

            System.out.println("* " + shortHash + " (HEAD -> " + branchName + ")  " + msg);
        } else {
            System.out.println("o " + shortHash + "  " + msg);
        }

        System.out.println("|");
    }

    private String extractMessage(String commitData) {
        for (String line : commitData.split("\n")) {
            if (line.startsWith("message ")) {
                return line.substring("message ".length()).trim();
            }
        }
        return "(no message)";
    }
}
