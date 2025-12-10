package org.example.commands;

public class HelpCommand implements Command {

    @Override
    public void execute(String[] args) {

        System.out.println("Fit Version Control System");
        System.out.println("--------------------------");
        System.out.println("Usage:");
        System.out.println();

        // INIT
        System.out.println("  fit init");
        System.out.println("      Create a new repository by generating the .fit directory.");
        System.out.println("      Example:");
        System.out.println("          fit init");
        System.out.println();

        // STATUS
        System.out.println("  fit status");
        System.out.println("      Show the state of your working directory:");
        System.out.println("      - Untracked: Not added yet");
        System.out.println("      - Staged: Ready to commit");
        System.out.println("      - Modified: Changed after staging");
        System.out.println("      - (deleted): File removed from disk but still tracked");
        System.out.println("      Example:");
        System.out.println("          fit status");
        System.out.println();

        // ADD
        System.out.println("  fit add <file | directory | .>");
        System.out.println("      Stage a file or folder recursively for commit.");
        System.out.println("      Examples:");
        System.out.println("          fit add Hello.txt");
        System.out.println("          fit add src/");
        System.out.println("          fit add .        (add everything)");
        System.out.println();

        // RM
        System.out.println("  fit rm <file>");
        System.out.println("      Unstage a file OR mark it as deleted if removed from disk.");
        System.out.println("      Examples:");
        System.out.println("          fit rm Hello.txt");
        System.out.println("          (removes Hello.txt from index)");
        System.out.println();

        // COMMIT
        System.out.println("  fit commit -m \"message\"");
        System.out.println("      Save staged changes as a new commit.");
        System.out.println("      Example:");
        System.out.println("          fit commit -m \"Added login service\"");
        System.out.println();

        // LOG
        System.out.println("  fit log");
        System.out.println("      Show linear commit history with full commit hashes.");
        System.out.println("      Example:");
        System.out.println("          fit log");
        System.out.println();

        System.out.println("  fit log --graph");
        System.out.println("      Show commits as an ASCII commit graph with HEAD pointer.");
        System.out.println("      Example:");
        System.out.println("          fit log --graph");
        System.out.println();

        // CHECKOUT
        System.out.println("  fit checkout <commitHash>");
        System.out.println("      Restore files from a specific commit (detached HEAD).");
        System.out.println("      Example:");
        System.out.println("          fit checkout a3fbd1290c9e...");
        System.out.println();

        // BRANCH
        System.out.println("  fit branch <name>");
        System.out.println("      Create a new branch pointing to the current commit.");
        System.out.println("      Example:");
        System.out.println("          fit branch feature");
        System.out.println();

        System.out.println("  fit branch --list");
        System.out.println("      List all branches inside .fit/refs/");
        System.out.println("      Example:");
        System.out.println("          fit branch --list");
        System.out.println();

        System.out.println("  fit branch --delete <name>");
        System.out.println("      Delete a branch (cannot delete current branch).");
        System.out.println("      Example:");
        System.out.println("          fit branch --delete feature");
        System.out.println();

        System.out.println("  fit branch --rename <old> <new>");
        System.out.println("      Rename an existing branch.");
        System.out.println("      Example:");
        System.out.println("          fit branch --rename feature new-feature");
        System.out.println();

        // SWITCH
        System.out.println("  fit switch <branch>");
        System.out.println("      Move HEAD to the given branch and restore its files.");
        System.out.println("      Example:");
        System.out.println("          fit switch feature");
        System.out.println();

        // PROMOTE
        System.out.println("  fit promote <branch>");
        System.out.println("      Fast-forward master to the given branch (only allowed if linear).");
        System.out.println("      Example:");
        System.out.println("          fit promote feature");
        System.out.println();

        // MERGE
        System.out.println("  fit merge <branch> [-m \"msg\"]");
        System.out.println("      Merge another branch into the current branch using 3-way merge.");
        System.out.println("      - If conflict detected, conflict markers will appear in files.");
        System.out.println("      Examples:");
        System.out.println("          fit merge feature");
        System.out.println("          fit merge feature -m \"Merge feature branch\"");
        System.out.println();

        // AI
        System.out.println("  fit ai \"natural language\"");
        System.out.println("      Ask the AI assistant to convert plain English into one FIT command.");
        System.out.println("      - AI never runs OS commands.");
        System.out.println("      - AI will refuse unsafe or unknown requests.");
        System.out.println("      - AI always returns exactly one FIT command.");
        System.out.println("      Examples:");
        System.out.println("          fit ai \"create a new branch for testing\"");
        System.out.println("              → fit branch testing");
        System.out.println();
        System.out.println("          fit ai \"switch to feature branch\"");
        System.out.println("              → fit switch feature");
        System.out.println();
        System.out.println("          fit ai \"show my project history\"");
        System.out.println("              → fit log --graph");
        System.out.println();
        System.out.println("          fit ai \"summarize last commit\"");
        System.out.println("              → fit explain <hash>");
        System.out.println();

        // HELP
        System.out.println("  fit help");
        System.out.println("      Show this help message.");
        System.out.println();
    }
}
