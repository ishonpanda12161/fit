package org.example.commands;

import org.example.Main;
import org.example.ai.AIClient;

public class AICommand implements Command {

    @Override
    public void execute(String[] args) {

        if (args.length == 0) {
            System.out.println("Usage: fit ai \"instruction\"");
            return;
        }

        String userPrompt = String.join(" ", args);

        String systemPrompt =
                "Translate the user's request into EXACTLY one valid FIT command.\n" +
                        "Only output the command, no explanations.\n" +
                        "If the user asks anything outside FIT, output: fit help\n" +
                        "\n" +
                        "VALID FIT COMMANDS:\n" +
                        "fit init\n" +
                        "fit status\n" +
                        "fit add <path>\n" +
                        "fit rm <file>\n" +
                        "fit commit -m \"msg\"\n" +
                        "fit log\n" +
                        "fit log --graph\n" +
                        "fit checkout <hash>\n" +
                        "fit branch <name>\n" +
                        "fit branch --list\n" +
                        "fit branch --delete <name>\n" +
                        "fit branch --rename <old> <new>\n" +
                        "fit switch <branch>\n" +
                        "fit promote <branch>\n" +
                        "fit merge <branch> -m \"msg\"\n" +
                        "fit explain <hash>\n" +
                        "\n" +
                        "RULES:\n" +
                        "• Never invent flags. NO '--create', NO extras.\n" +
                        "• Output MUST start with: fit\n" +
                        "• If unsure → output 'fit help'\n";

        try {
            String raw = AIClient.ask(systemPrompt, userPrompt).trim();

            // cleanup
            raw = raw.replace("\n", " ").trim();

            // must start with fit
            if (!raw.startsWith("fit")) {
                raw = "fit help";
            }

            // Remove illegal flags like "--create"
            raw = raw.replace("--create", "").trim();
            raw = raw.replaceAll("\\s+", " ");

            System.out.println("AI → " + raw);

            // split tokens
            String[] toks = raw.split(" ");

            // remove the "fit" prefix so Main.dispatch only sees real command
            String[] runArgs = new String[toks.length - 1];
            System.arraycopy(toks, 1, runArgs, 0, toks.length - 1);

            // execute
            Main.dispatch(runArgs);

        } catch (Exception e) {
            System.out.println("AI failed.");
            e.printStackTrace();
        }
    }
}
