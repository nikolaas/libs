package org.ns.util;

import java.io.File;

/**
 *
 * @author stupak
 */
public class FileUtils {

    private FileUtils() {
    }
    
    public static String getExtension(File file) {
        return getExtension(file.getAbsolutePath());
    }
    
    public static String getExtension(String filePath) {
        int dotIndex = filePath.lastIndexOf(".");
        if ( dotIndex < 0  ) {
            return null;
        }
        return filePath.substring(dotIndex + 1);
    }
}
