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

        try {
            File branchFile = new File(".fit/refs/" + branch);
            if (!branchFile.exists()) {
                System.out.println("Branch not found: " + branch);
                return;
            }

            String commitHash = Files.readString(branchFile.toPath()).trim();

            if (commitHash.isBlank()) {
                System.out.println("Branch has no commits yet.");
                return;
            }

            CheckoutCommand checkout = new CheckoutCommand();
            checkout.checkoutToWorktree(commitHash);

            Files.writeString(new File(".fit/HEAD").toPath(), branch);

            System.out.println("Switched to branch " + branch);

        } catch (Exception e) {
            System.out.println("Switch failed.");
            e.printStackTrace();
        }
    }
}
