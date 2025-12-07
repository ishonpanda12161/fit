package org.example.commands;

public class HelpCommand implements Command{
    @Override
    public void execute(String[] args) {
        System.out.println("Usage:");
        System.out.println("  fit init");
        System.out.println("  fit add <file>");
        System.out.println("  fit commit -m \"msg\"");
        System.out.println("  fit log");
        System.out.println("  fit checkout <hash>");
    }
}
