package org.example.core;

import org.example.Utils.HashUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ObjectStore {

    private static final String OBJECTS_DIR = ".fit/objects/";

    public static String store(byte[] data) throws IOException
    {
        String hash = HashUtil.sha256(data);

        File out = new File(OBJECTS_DIR + hash);

        if(!out.exists())
        {
            try(FileOutputStream fos = new FileOutputStream(out)){
                fos.write(data);
            }
        }

        return hash;
    }

    public static byte[] read(String hash) throws IOException
    {
        File file = new File(OBJECTS_DIR + hash);

        return java.nio.file.Files.readAllBytes(file.toPath());
    }
}
