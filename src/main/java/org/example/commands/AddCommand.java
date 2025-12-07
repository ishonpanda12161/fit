package org.example.commands;

import org.example.core.Index;
import org.example.core.ObjectStore;

import java.io.File;
import java.nio.file.Files;
import java.util.Map;

public class AddCommand implements Command{
    @Override
    public void execute(String[] args) {
        try{
            if(args.length==0)
            {
                System.out.println("Error: No file specified");
                return;
            }

            String filePath = args[0];
            File file = new File(filePath);

            if(!file.exists())
            {
                System.out.println("Error: file not found -> "+filePath);
                return;
            }

            byte[] data = Files.readAllBytes(file.toPath());

            String hash = ObjectStore.store(data);

            Map<String,String> index = Index.load();

            index.put(filePath,hash);
            Index.save(index);

            System.out.println("Added '"+filePath+"' -> "+hash);
        }catch (Exception e)
        {
            System.out.println("Error: Failed to add file/s");
            e.printStackTrace();
        }
    }
}
