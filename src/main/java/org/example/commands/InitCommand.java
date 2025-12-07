package org.example.commands;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.FileNameMap;

public class InitCommand implements Command{
    @Override
    public void execute(String[] args) {
        File fitDir = new File(".fit");
        if(fitDir.exists())
        {
            System.out.println("Fit repository already initialized : ) ");
            return;
        }

        if(!fitDir.mkdir())
        {
            System.out.println("Error: Could not create .fit directory.");
            return;
        }

        File objectsDir = new File(".fit/objects");
        File refsDir = new File(".fit/refs");

        objectsDir.mkdir();
        refsDir.mkdir();

        try{
            File head = new File(".fit/HEAD");
            FileWriter writer = new FileWriter(head);
            writer.write("ref: refs/master");
            writer.close();
        }catch (IOException e)
        {
            System.out.println("Error writing HEAD file.");
            e.printStackTrace();
        }

        try{
            File master = new File(".fit/refs/master");
            master.createNewFile();
        }catch (IOException e)
        {
            System.out.println("Error creating master branch file");
            e.printStackTrace();
        }

        System.out.println("Initialized FIT repository in : "+fitDir.getAbsolutePath());
    }
}
