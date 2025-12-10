package org.example.Utils;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class IgnoreUtil {

    private static final List<String> patterns = new ArrayList<>();
    private static boolean loaded = false;

    public static void loadIgnoreFile() {
        if (loaded) return;
        loaded = true;

        File ignore = new File(".fitignore");
        if (!ignore.exists()) return;

        try {
            for (String line : Files.readAllLines(ignore.toPath())) {
                line = line.trim();
                if (line.isBlank() || line.startsWith("#")) continue;
                patterns.add(line);
            }
        } catch (Exception ignored) {}
    }

    public static boolean shouldIgnore(String path) {
        loadIgnoreFile();

        for (String p : patterns) {
            if (matches(p, path)) return true;
        }

        return false;
    }

    private static boolean matches(String pattern, String path) {
        // wildcard: *.txt
        if (pattern.startsWith("*.")) {
            return path.endsWith(pattern.substring(1));
        }

        // folder ignore
        if (pattern.endsWith("/")) {
            return path.startsWith(pattern.substring(0, pattern.length()-1));
        }

        // exact match
        return path.equals(pattern);
    }
}
