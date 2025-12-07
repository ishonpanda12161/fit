package org.example.core;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Index {

    private static final String INDEX_PATH = ".fit/index";

    public static Map<String,String> load() throws IOException
    {
        Map<String,String> map = new HashMap<>();

        File indexFile = new File(INDEX_PATH);
        if(!indexFile.exists())
        {
            return map;
        }

        try(BufferedReader br = new BufferedReader(new FileReader(indexFile)))
        {
            String line;
            while((line = br.readLine())!=null)
            {
                String[] parts = line.split(" ");
                if(parts.length==2)
                {
                    map.put(parts[0],parts[1]);
                }
            }
        }

        return map;
    }

    public static void save(Map<String, String> map) throws IOException {
        File indexFile = new File(INDEX_PATH);

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(indexFile))) {
            for (var entry : map.entrySet()) {
                bw.write(entry.getKey() + " " + entry.getValue());
                bw.newLine();
            }
        }
    }
}
