package org.example.commands;

import java.io.File;
import java.nio.file.Files;

public class SwitchCommand implements Command {

    @Override
    public void execute(String[] args) {

        if (args.length != 1) {
            System.out.println("Usage: fit switch <branch>");
            return;
        }

        String branch = args[0];
        File ref = new File(".fit/refs/" + branch);

        if (!ref.exists()) {
            System.out.println("Branch not found: " + branch);
            return;
        }

        try {
            String commitHash = Files.readString(ref.toPath()).trim();

            // Restore committed snapshot
            new CheckoutCommand().checkoutToWorktree(commitHash);

            // Set HEAD to branch
            Files.writeString(new File(".fit/HEAD").toPath(), "ref: refs/" + branch);

            System.out.println("Switched to branch " + branch);

        } catch (Exception e) {
            System.out.println("Switch failed.");
            e.printStackTrace();
        }
    }
}
