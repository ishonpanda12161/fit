package org.example.commands;

import org.example.core.Index;
import org.example.core.ObjectStore;
import org.example.Utils.MergeUtil;
import org.example.Utils.TreeUtil;
import org.example.Utils.ThreeWayMerger;
import org.example.Utils.HashUtil;

import java.io.File;
import java.nio.file.Files;
import java.util.*;

/**
 * Usage:
 *   fit merge <branch-to-merge> [-m "message"]
 *
 * Behavior:
 *  - Finds current branch (HEAD must point to a branch)
 *  - Reads commit hashes: ours (current branch head) and theirs (.fit/refs/<branch>)
 *  - Finds common ancestor (base)
 *  - For every file in union(base, ours, theirs) perform 3-way merge
 *  - Writes merged files to working dir, stores blobs, builds new tree
 *  - Creates a commit with two parents (ours, theirs)
 *  - Updates current branch ref and index
 *  - Prints whether conflicts occurred
 */
public class MergeCommand implements Command {

    @Override
    public void execute(String[] args) {

        if (args.length < 1) {
            System.out.println("Usage: fit merge <branch> [-m \"message\"]");
            return;
        }

        String branchToMerge = args[0];


        String message = "Merge branch " + branchToMerge;
        if (args.length >= 3 && "-m".equals(args[1])) {
            message = args[2];
        }

        try {

            String headRef = MergeUtil.currentBranchRef();
            String currentBranchFile = ".fit/" + headRef;
            String ourCommit = MergeUtil.readRefCommit(headRef);
            if (ourCommit == null) {
                System.out.println("Current branch has no commits to merge from.");
                return;
            }


            File theirRefFile = new File(".fit/refs/" + branchToMerge);
            if (!theirRefFile.exists()) {
                System.out.println("Branch to merge not found: " + branchToMerge);
                return;
            }
            String theirCommit = Files.readString(theirRefFile.toPath()).trim();
            if (theirCommit.isEmpty()) {
                System.out.println("Branch " + branchToMerge + " has no commits.");
                return;
            }


            String baseCommit = MergeUtil.findCommonAncestor(ourCommit, theirCommit);

            Map<String, String> baseTree = (baseCommit == null) ? new HashMap<>() : TreeUtil.readTree(MergeUtil.extractTree(MergeUtil.readCommit(baseCommit)));
            Map<String, String> ourTree  = TreeUtil.readTree(MergeUtil.extractTree(MergeUtil.readCommit(ourCommit)));
            Map<String, String> theirTree= TreeUtil.readTree(MergeUtil.extractTree(MergeUtil.readCommit(theirCommit)));


            Set<String> allFiles = new HashSet<>();
            allFiles.addAll(baseTree.keySet());
            allFiles.addAll(ourTree.keySet());
            allFiles.addAll(theirTree.keySet());

            boolean anyConflicts = false;
            Map<String, String> mergedTree = new HashMap<>();


            for (String file : allFiles) {
                String baseHash = baseTree.get(file);
                String ourHash  = ourTree.get(file);
                String theirHash= theirTree.get(file);


                if (Objects.equals(ourHash, theirHash)) {
                    if (ourHash != null) mergedTree.put(file, ourHash);
                    continue;
                }


                if (Objects.equals(baseHash, ourHash) && Objects.equals(baseHash, theirHash)) {
                    if (baseHash != null) mergedTree.put(file, baseHash);
                    continue;
                }


                if (!Objects.equals(baseHash, ourHash) && Objects.equals(baseHash, theirHash)) {

                    if (ourHash != null) mergedTree.put(file, ourHash);
                    continue;
                }
                if (!Objects.equals(baseHash, theirHash) && Objects.equals(baseHash, ourHash)) {

                    if (theirHash != null) mergedTree.put(file, theirHash);
                    continue;
                }


                if (ourHash == null && theirHash != null) {
                    mergedTree.put(file, theirHash);
                    continue;
                }
                if (theirHash == null && ourHash != null) {

                    mergedTree.put(file, ourHash);
                    continue;
                }
                if (ourHash == null && theirHash == null) {

                    continue;
                }


                byte[] baseBytes = (baseHash == null) ? new byte[0] : ObjectStore.read(baseHash);
                byte[] ourBytes  = ObjectStore.read(ourHash);
                byte[] theirBytes= ObjectStore.read(theirHash);

                String baseText = new String(baseBytes);
                String ourText  = new String(ourBytes);
                String theirText= new String(theirBytes);

                ThreeWayMerger.MergeResult result = ThreeWayMerger.mergeText(baseText, ourText, theirText);

                if (!result.conflict) {

                    String mergedHash = ObjectStore.store(result.text.getBytes());
                    mergedTree.put(file, mergedHash);
                } else {

                    anyConflicts = true;

                    Files.writeString(new File(file).toPath(), result.text);

                    String mergedHash = ObjectStore.store(result.text.getBytes());
                    mergedTree.put(file, mergedHash);
                }
            }


            String mergedTreeHash = TreeUtil.writeTree(mergedTree);


            String commitData = buildMergeCommit(mergedTreeHash, ourCommit, theirCommit, message);
            String commitHash = ObjectStore.store(commitData.getBytes());


            Files.writeString(new File(currentBranchFile).toPath(), commitHash);

            Index.save(mergedTree);

            System.out.println("Merge completed. Commit: " + commitHash + (anyConflicts ? " (with conflicts)" : ""));
            if (anyConflicts) {
                System.out.println("Resolve conflicts in working directory, then commit the resolution.");
            }

        } catch (Exception e) {
            System.out.println("Merge failed.");
            e.printStackTrace();
        }
    }

    private String buildMergeCommit(String treeHash, String ourParent, String theirParent, String message) {
        StringBuilder sb = new StringBuilder();
        sb.append("tree ").append(treeHash).append("\n");
        // two parent lines
        sb.append("parent ").append(ourParent).append("\n");
        sb.append("parent ").append(theirParent).append("\n");
        sb.append("message ").append(message).append("\n");
        sb.append("timestamp ").append(System.currentTimeMillis()).append("\n");
        return sb.toString();
    }
}
