package org.ns.util;

import java.text.MessageFormat;

/**
 *
 * @author stupak
 */
public class Assert {

    private Assert() {
    }
    
    public static <T> T isNotNull(T object, String message) {
        if ( object == null ) {
            throw new NullPointerException(message);
        }
        return object;
    }
    
    public static void isNull(Object object, String message) {
        if ( object != null ) {
            throw new NullPointerException(message);
        }
    }
    
    public static void isTrue(boolean value, String mesString, Object... params) {
        if ( !value ) {
            String mes = MessageFormat.format(mesString, params);
            throw new RuntimeException(mes);
        }
    }
    
    public static void isFalse(boolean value, String mesString) {
        if ( value ) {
            throw new RuntimeException(mesString);
        }
    }
}
