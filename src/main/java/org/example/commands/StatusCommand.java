package org.example.commands;

import org.example.Utils.IgnoreUtil;
import org.example.core.Index;
import org.example.Utils.HashUtil;

import java.io.File;
import java.nio.file.Files;
import java.util.*;

public class StatusCommand implements Command {

    @Override
    public void execute(String[] args) {

        try {
            Map<String, String> index = Index.load();

            List<String> workingFiles = new ArrayList<>();
            collectFiles(new File("."), workingFiles);

            List<String> staged = new ArrayList<>();
            List<String> modified = new ArrayList<>();
            List<String> untracked = new ArrayList<>();

            for (String file : workingFiles) {

                if (!index.containsKey(file)) {
                    untracked.add(file);
                    continue;
                }

                String stagedHash = index.get(file);
                byte[] content = Files.readAllBytes(new File(file).toPath());
                String liveHash = HashUtil.sha256(content);

                if (liveHash.equals(stagedHash))
                    staged.add(file);
                else
                    modified.add(file);
            }

            for (String file : index.keySet()) {
                if (!workingFiles.contains(file))
                    modified.add(file + " (deleted)");
            }

            if (staged.isEmpty() && modified.isEmpty() && untracked.isEmpty()) {
                System.out.println("Nothing to commit, working directory clean.");
                return;
            }

            if (!staged.isEmpty()) {
                System.out.println("Staged:");
                for (String f : staged) System.out.println("  " + f);
            }

            if (!modified.isEmpty()) {
                System.out.println("Modified:");
                for (String f : modified) System.out.println("  " + f);
            }

            if (!untracked.isEmpty()) {
                System.out.println("Untracked:");
                for (String f : untracked) System.out.println("  " + f);
            }

        } catch (Exception e) {
            System.out.println("Status failed.");
            e.printStackTrace();
        }
    }

    private void collectFiles(File root, List<String> list) throws Exception {
        File[] files = root.listFiles();
        if (files == null) return;

        String projectRoot = new File(".").getCanonicalPath().replace("\\", "/");

        for (File f : files) {

            if (f.getName().equals(".fit")) continue;
            if (f.getName().endsWith(".jar")) continue;
            if (IgnoreUtil.shouldIgnore(f.getName())) continue;

            if (f.isDirectory()) {
                collectFiles(f, list);
            } else {
                String canonical = f.getCanonicalPath().replace("\\", "/");
                String normalized = canonical.replace(projectRoot + "/", "");
                list.add(normalized);
            }
        }
    }
}
