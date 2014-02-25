package org.ns.util;

/**
 *
 * @author stupak
 */
public class Strings {

    private static final String EMPTY_STRING = "";
    private Strings() {
    }
    
    public static boolean empty(String str) {
        return str == null || EMPTY_STRING.equals(str);
    }
}
