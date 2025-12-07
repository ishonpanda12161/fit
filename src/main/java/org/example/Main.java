package org.example;

import org.example.Utils.HashUtil;
import org.example.commands.AddCommand;
import org.example.commands.HelpCommand;
import org.example.commands.InitCommand;

import java.util.Arrays;

/**
 * Hello world!
 *
 */
public class Main
{
    public static void main( String[] args )
    {
        if(args.length==0)
        {
            new HelpCommand().execute(args);
            return;
        }

        String command = args[0];

        switch(command){
            case "add":
                new AddCommand().execute(trimArgs(args));
                break;
            case "testhello":
                System.out.println(HashUtil.sha256("hello".getBytes()));
                break;
            case "init":
                new InitCommand().execute(trimArgs(args));
                break;

            case "help":
                new HelpCommand().execute(trimArgs(args));
                break;

            default:
                System.out.println("Unknown command + "+command);
                new HelpCommand().execute(args);
        }

    }

    private static String[] trimArgs(String[] args)
    {
        if(args.length<=1)
        {
            return new String[0];

        }
        String[] trimmed = new String[args.length-1];
        System.arraycopy(args,1,trimmed,0,args.length-1);
        return trimmed;
    }

}
