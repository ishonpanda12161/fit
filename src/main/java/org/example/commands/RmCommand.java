package org.example.commands;

import org.example.core.Index;

import java.io.File;
import java.nio.file.Files;
import java.util.Map;

public class RmCommand implements Command {

    @Override
    public void execute(String[] args) {

        if (args.length != 1) {
            System.out.println("Usage: fit rm <file>");
            return;
        }

        String fileName = args[0];

        try {

            Map<String, String> index = Index.load();

            if (!index.containsKey(fileName)) {
                System.out.println("File is not staged: " + fileName);
            } else {
                index.remove(fileName);
                System.out.println("Removed from index: " + fileName);
            }


            Index.save(index);

            File file = new File(fileName);
            if (file.exists()) {
                if (file.delete()) {
                    System.out.println("Deleted from working directory: " + fileName);
                } else {
                    System.out.println("Could not delete file from working directory.");
                }
            }

        } catch (Exception e) {
            System.out.println("rm failed.");
            e.printStackTrace();
        }
    }
}
