package org.example.commands;

import org.example.ai.AIClient;

public class AITest implements Command {

    @Override
    public void execute(String[] args) {
        try {
            String system = "You are FIT-AI diagnostic.";
            String user = "Return the text FIT-TEST only.";

            String reply = AIClient.ask(system, user);
            System.out.println("AI SAYS: " + reply);

        } catch (Exception e) {
            System.out.println("AITest failed.");
            e.printStackTrace();
        }
    }
}
