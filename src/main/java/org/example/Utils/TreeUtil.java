package org.example.Utils;

import org.example.core.ObjectStore;

import java.io.File;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

/**
 * Read and write simple tree objects (line-based "filename hash" format).
 */
public class TreeUtil {


    public static Map<String, String> readTree(String treeHash) throws Exception {
        Map<String, String> map = new HashMap<>();
        if (treeHash == null || treeHash.equals("null")) return map;

        File f = new File(".fit/objects/" + treeHash);
        if (!f.exists()) return map;

        String content = Files.readString(f.toPath());
        for (String line : content.split("\n")) {
            if (line.isBlank()) continue;
            String[] parts = line.split(" ", 2);
            if (parts.length == 2) {
                map.put(parts[0], parts[1]);
            }
        }
        return map;
    }


    public static String writeTree(Map<String, String> map) throws Exception {
        StringBuilder sb = new StringBuilder();
        // sort keys for determinism
        java.util.List<String> keys = new java.util.ArrayList<>(map.keySet());
        java.util.Collections.sort(keys);
        for (String k : keys) {
            sb.append(k).append(" ").append(map.get(k)).append("\n");
        }
        byte[] bytes = sb.toString().getBytes();
        return ObjectStore.store(bytes);
    }
}
