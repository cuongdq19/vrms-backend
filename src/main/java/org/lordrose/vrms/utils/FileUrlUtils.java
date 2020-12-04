package org.lordrose.vrms.utils;

public class FileUrlUtils {

    public static String[] getUrlsAsArray(String imageUrls) {
        if (imageUrls == null) {
            return new String[0];
        }
        if (!imageUrls.trim().isEmpty()) {
            imageUrls = imageUrls.replaceAll("\\|\\|", " ").strip();
            return imageUrls.split(" ");
        }
        return new String[0];
    }
}
