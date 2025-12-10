package org.example.commands;

import java.io.File;
import java.nio.file.Files;

public class LogCommand implements Command {

    @Override
    public void execute(String[] args) {

        try {

            File master = new File(".fit/refs/master");
            if (!master.exists() || master.length() == 0) {
                System.out.println("No commits yet.");
                return;
            }

            String commitHash = Files.readString(master.toPath()).trim();


            while (!commitHash.equals("null")) {


                File commitObj = new File(".fit/objects/" + commitHash);

                if (!commitObj.exists()) {
                    System.out.println("Corrupted history: missing commit " + commitHash);
                    return;
                }

                String commitData = Files.readString(commitObj.toPath());
                System.out.println("commit " + commitHash);


                printCommit(commitData);


                commitHash = extractParent(commitData);
            }

        } catch (Exception e) {
            System.out.println("Failed to read log.");
            e.printStackTrace();
        }
    }


    private void printCommit(String data) {
        String[] lines = data.split("\n");
        for (String line : lines) {
            if (line.startsWith("message")) {
                System.out.println("    " + line);
            } else if (line.startsWith("timestamp")) {
                System.out.println("    " + line);
            }
        }
        System.out.println();
    }

    private String extractParent(String data) {
        for (String line : data.split("\n")) {
            if (line.startsWith("parent")) {
                return line.substring(7).trim();
            }
        }
        return "null";
    }
}
