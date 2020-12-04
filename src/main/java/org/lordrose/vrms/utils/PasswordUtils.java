package org.lordrose.vrms.utils;

public class PasswordUtils {

    private static final String CHARACTER_CONTAINER = "abcdefghijklmnopqrstuvxyz" +
            "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
            "0123456789";

    public static String getRandomAlphabeticWithLength(int length) {
        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int randomIndex = (int) (CHARACTER_CONTAINER.length()*Math.random());
            sb.append(CHARACTER_CONTAINER.charAt(randomIndex));
        }
        return sb.toString();
    }
}
