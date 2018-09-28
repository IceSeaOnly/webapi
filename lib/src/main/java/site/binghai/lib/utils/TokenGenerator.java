package site.binghai.lib.utils;


import java.util.UUID;

public class TokenGenerator {

    public static String generate(int len) {
        String uuid = UUID.randomUUID().toString();
        len = Math.min(len, uuid.length());
        return uuid.substring(0, len).toUpperCase();
    }

}
