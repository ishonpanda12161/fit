package org.example;

import org.example.commands.*;

public class Main {

    public static void main(String[] args) {

        if (args.length == 0) {
            new HelpCommand().execute(args);
            return;
        }

        String command = args[0];

        switch (command) {

            case "add":
                new AddCommand().execute(trimArgs(args));
                break;

            case "about":
                System.out.println("Created by github.com/ishonpanda12161/fit . Please Star ^_^");
                break;

            case "init":
                new InitCommand().execute(trimArgs(args));
                break;

            case "help":
                new HelpCommand().execute(trimArgs(args));
                break;

            case "commit":
                new CommitCommand().execute(trimArgs(args));
                break;

            case "checkout":
                new CheckoutCommand().execute(trimArgs(args));
                break;

            case "status":
                new StatusCommand().execute(trimArgs(args));
                break;

            case "branch":
                new BranchCommand().execute(trimArgs(args));
                break;

            case "switch":
                new SwitchCommand().execute(trimArgs(args));
                break;

            case "promote":
                new PromoteCommand().execute(trimArgs(args));
                break;

            case "rm":
                new RmCommand().execute(trimArgs(args));
                break;

            case "merge":
                new MergeCommand().execute(trimArgs(args));
                break;

            case "log":
                if (args.length > 1 && args[1].equals("--graph")) {
                    new GraphLogCommand().execute(trimArgs(args));
                } else {
                    new LogCommand().execute(trimArgs(args));
                }
                break;

            case "ai":
                new AICommand().execute(trimArgs(args));
                break;

            case "explain":
                new ExplainCommand().execute(trimArgs(args));
                break;

            case "aitest":
                new AITest().execute(trimArgs(args));
                break;

            default:
                System.out.println("Unknown command + " + command);
                new HelpCommand().execute(args);
        }
    }

    private static String[] trimArgs(String[] args) {

        if (args.length <= 1) return new String[0];

        String[] trimmed = new String[args.length - 1];
        System.arraycopy(args, 1, trimmed, 0, trimmed.length);
        return trimmed;
    }


    public static void dispatch(String[] args) {
        if (args.length == 0) {
            new HelpCommand().execute(new String[0]);
            return;
        }

        String command = args[0];
        String[] trimmed = trimArgs(args);

        switch (command) {
            case "init": new InitCommand().execute(trimmed); break;
            case "add": new AddCommand().execute(trimmed); break;
            case "status": new StatusCommand().execute(trimmed); break;
            case "commit": new CommitCommand().execute(trimmed); break;
            case "branch": new BranchCommand().execute(trimmed); break;
            case "switch": new SwitchCommand().execute(trimmed); break;
            case "rm": new RmCommand().execute(trimmed); break;
            case "promote": new PromoteCommand().execute(trimmed); break;
            case "merge": new MergeCommand().execute(trimmed); break;
            case "checkout": new CheckoutCommand().execute(trimmed); break;
            case "log":
                if (trimmed.length > 0 && trimmed[0].equals("--graph"))
                    new GraphLogCommand().execute(trimmed);
                else
                    new LogCommand().execute(trimmed);
                break;
            case "explain": new ExplainCommand().execute(trimmed); break;
            case "help": new HelpCommand().execute(trimmed); break;
            default:
                System.out.println("Unknown command: " + command);
                new HelpCommand().execute(new String[0]);
        }
    }


}
