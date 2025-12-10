package org.example.commands;

import org.example.Utils.IgnoreUtil;
import org.example.core.Index;
import org.example.core.ObjectStore;

import java.io.File;
import java.nio.file.Files;
import java.util.Map;

public class AddCommand implements Command {

    @Override
    public void execute(String[] args) {

        if (args.length == 0) {
            System.out.println("Error: No file specified");
            return;
        }

        String target = args[0];
        File f = new File(target);

        try {
            Map<String, String> index = Index.load();
            addPath(f, index);
            Index.save(index);
        } catch (Exception e) {
            System.out.println("Error: Failed to add file/s");
            e.printStackTrace();
        }
    }

    private void addPath(File f, Map<String, String> index) throws Exception {

        if (!f.exists()) return;

        if (f.isDirectory()) {
            if (f.getName().equals(".fit")) return;
            for (File child : f.listFiles()) addPath(child, index);
            return;
        }

        if (IgnoreUtil.shouldIgnore(f.getName())) return;
        if (f.getName().endsWith(".jar")) return;

        String projectRoot = new File(".").getCanonicalPath().replace("\\", "/");
        String canonical = f.getCanonicalPath().replace("\\", "/");
        String normalized = canonical.replace(projectRoot + "/", "");

        byte[] content = Files.readAllBytes(f.toPath());
        String hash = ObjectStore.store(content);

        index.put(normalized, hash);
    }
}
