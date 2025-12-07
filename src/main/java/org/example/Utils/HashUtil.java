package org.example.Utils;

import java.security.MessageDigest;

public class HashUtil {

    public static String sha256(byte[] data)
    {
        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(data);
            return bytesToHex(hashBytes);
        }catch (Exception e)
        {
            throw new RuntimeException("SHA-256 hashing failed");
        }
    }

    private static String bytesToHex(byte[] bytes)
    {
        StringBuilder sb = new StringBuilder();
        for(byte b : bytes)
        {
            sb.append(String.format("%02x",b));
        }

        return sb.toString();
    }

}
