package org.example.commands;

import java.io.File;
import java.nio.file.Files;

public class PromoteCommand implements Command {

    @Override
    public void execute(String[] args) {

        if (args.length != 1) {
            System.out.println("Usage: fit promote <branch>");
            return;
        }

        String branch = args[0];

        try {
            File branchRef = new File(".fit/refs/" + branch);

            if (!branchRef.exists()) {
                System.out.println("Branch does not exist.");
                return;
            }

            // Read commit hash from that branch
            String commitHash = Files.readString(branchRef.toPath()).trim();

            // Overwrite master pointer
            Files.writeString(new File(".fit/refs/master").toPath(), commitHash);

            // Update HEAD to point back to master
            Files.writeString(new File(".fit/HEAD").toPath(), "ref: refs/master");

            // Restore working directory to this commit
            new CheckoutCommand().execute(new String[]{commitHash});

            System.out.println("Promoted branch '" + branch + "' to master.");

        } catch (Exception e) {
            System.out.println("Promotion failed.");
            e.printStackTrace();
        }
    }
}
