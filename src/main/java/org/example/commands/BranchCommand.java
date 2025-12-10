package org.example.commands;

import java.io.File;
import java.nio.file.Files;
import java.util.Objects;

public class BranchCommand implements Command {

    @Override
    public void execute(String[] args) {

        if (args.length == 0) {
            listBranches();
            return;
        }

        boolean delete = contains(args, "--delete", "-d");
        boolean rename = contains(args, "--rename");
        boolean list = contains(args, "--list", "-l");

        if (list) {
            listBranches();
            return;
        }

        if (delete) {
            String name = pickName(args, "--delete", "-d");
            if (name == null) {
                System.out.println("Usage: fit branch --delete <name>");
                return;
            }
            deleteBranch(name);
            return;
        }

        if (rename) {
            String[] names = pickTwoNames(args, "--rename");
            if (names == null) {
                System.out.println("Usage: fit branch --rename <old> <new>");
                return;
            }
            renameBranch(names[0], names[1]);
            return;
        }

        if (args.length == 1) {
            createBranch(args[0]);
            return;
        }

        System.out.println("Invalid branch command.");
    }

    private boolean contains(String[] args, String... flags) {
        for (String a : args) {
            for (String f : flags) {
                if (a.equals(f)) return true;
            }
        }
        return false;
    }

    private String pickName(String[] args, String... flags) {
        for (int i = 0; i < args.length; i++) {
            for (String f : flags) {
                if (args[i].equals(f)) {
                    if (i + 1 < args.length) return args[i + 1];
                    if (i - 1 >= 0) return args[i - 1];
                }
            }
        }
        return null;
    }

    private String[] pickTwoNames(String[] args, String flag) {
        int pos = -1;
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals(flag)) pos = i;
        }
        if (pos == -1) return null;

        if (pos + 2 < args.length) return new String[]{args[pos + 1], args[pos + 2]};
        if (pos >= 2) return new String[]{args[pos - 2], args[pos - 1]};

        return null;
    }

    private void createBranch(String name) {
        try {
            String head = Files.readString(new File(".fit/HEAD").toPath()).trim();
            if (head.startsWith("detached")) {
                System.out.println("Cannot create branch in detached HEAD.");
                return;
            }
            String current = head.substring(5).trim();
            String commitHash = Files.readString(new File(".fit/" + current).toPath()).trim();

            File newBranch = new File(".fit/refs/" + name);
            if (newBranch.exists()) {
                System.out.println("Branch already exists.");
                return;
            }
            Files.writeString(newBranch.toPath(), commitHash);
            System.out.println("Created branch " + name);

        } catch (Exception e) {
            System.out.println("Failed to create branch.");
        }
    }

    private void listBranches() {
        try {
            File refs = new File(".fit/refs");
            System.out.println("Branches:");
            for (File f : Objects.requireNonNull(refs.listFiles())) {
                System.out.println("  " + f.getName());
            }
        } catch (Exception e) {
            System.out.println("Failed to list branches.");
        }
    }

    private void deleteBranch(String name) {
        try {
            File b = new File(".fit/refs/" + name);
            if (!b.exists()) {
                System.out.println("Branch not found.");
                return;
            }
            String head = Files.readString(new File(".fit/HEAD").toPath()).trim();
            if (head.equals("ref: refs/" + name)) {
                System.out.println("Cannot delete the branch you're on.");
                return;
            }
            b.delete();
            System.out.println("Deleted branch " + name);
        } catch (Exception e) {
            System.out.println("Failed to delete branch.");
        }
    }

    private void renameBranch(String oldName, String newName) {
        try {
            File old = new File(".fit/refs/" + oldName);
            File neu = new File(".fit/refs/" + newName);
            if (!old.exists()) {
                System.out.println("Branch not found.");
                return;
            }
            if (neu.exists()) {
                System.out.println("Target name exists.");
                return;
            }
            if (old.renameTo(neu)) {
                System.out.println("Renamed " + oldName + " to " + newName);
            } else {
                System.out.println("Rename failed.");
            }
        } catch (Exception e) {
            System.out.println("Rename failed.");
        }
    }
}
