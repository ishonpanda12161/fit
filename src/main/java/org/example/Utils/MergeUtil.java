package org.example.Utils;

import java.io.File;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.Set;

/**
 * Helpers to read commits, extract parent(s), tree, and find common ancestor.
 */
public class MergeUtil {

    // read commit object text
    public static String readCommit(String commitHash) throws Exception {
        File f = new File(".fit/objects/" + commitHash);
        if (!f.exists()) throw new IllegalArgumentException("Commit object not found: " + commitHash);
        return Files.readString(f.toPath());
    }

    // extract first parent or null
    public static String[] extractParents(String commitData) {
        // return array of parent hashes (maybe multiple lines "parent <hash>")
        String[] lines = commitData.split("\n");
        return java.util.Arrays.stream(lines)
                .map(String::trim)
                .filter(l -> l.startsWith("parent"))
                .map(l -> l.substring(6).trim())
                .filter(s -> !s.isEmpty() && !"null".equals(s))
                .toArray(String[]::new);
    }

    public static String extractTree(String commitData) {
        for (String line : commitData.split("\n")) {
            if (line.startsWith("tree ")) return line.substring(5).trim();
        }
        return null;
    }

    // find merge-base using simple ancestor walk (works for our linear-ish histories)
    // This method builds ancestor sets for each side and finds a first common.
    public static String findCommonAncestor(String aHash, String bHash) throws Exception {
        if (aHash == null || bHash == null) return null;
        // Build ancestors for A
        Set<String> ancestorsA = new HashSet<>();
        String cur = aHash;
        while (cur != null && !cur.equals("null")) {
            ancestorsA.add(cur);
            String data = readCommit(cur);
            String[] parents = extractParents(data);
            cur = parents.length > 0 ? parents[0] : "null";
        }

        // Walk ancestors of B and return first match
        cur = bHash;
        while (cur != null && !cur.equals("null")) {
            if (ancestorsA.contains(cur)) return cur;
            String data = readCommit(cur);
            String[] parents = extractParents(data);
            cur = parents.length > 0 ? parents[0] : "null";
        }

        return null; // no common ancestor found
    }

    // Determine current branch ref file path (e.g., refs/master)
    public static String currentBranchRef() throws Exception {
        File head = new File(".fit/HEAD");
        if (!head.exists()) throw new IllegalStateException("HEAD missing");
        String headData = Files.readString(head.toPath()).trim();
        if (!headData.startsWith("ref:")) throw new IllegalStateException("HEAD is detached");
        return headData.substring(5).trim();
    }

    // read commit hash pointed by a branch ref (like refs/master)
    public static String readRefCommit(String refPath) throws Exception {
        File f = new File(".fit/" + refPath);
        if (!f.exists()) return null;
        String s = Files.readString(f.toPath()).trim();
        return s.isEmpty() ? null : s;
    }
}
