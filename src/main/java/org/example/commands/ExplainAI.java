package org.example.commands;

import org.example.ai.AIClient;

public class ExplainAI {

    public static String summarize(String hash, String diff) throws Exception {

        String system =
                "You summarize version control commits.\n"
                        + "Input is a diff extracted from a commit.\n"
                        + "Output only a human readable explanation.\n"
                        + "Do not output commands, code, or hashes.\n";

        String user = "Summarize commit " + hash + ". Here is the diff:\n" + diff;

        return AIClient.ask(system, user);
    }
}
