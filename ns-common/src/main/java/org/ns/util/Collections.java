package org.ns.util;

import java.util.Collection;
import java.util.Map;

/**
 *
 * @author stupak
 */
public class Collections {

    private Collections() {
    }
    
    public static boolean isEmpty(Collection list) {
        return list == null || list.isEmpty();
    }
    
    public static boolean isEmpty(Map map) {
        return map == null || map.isEmpty();
    }
    
    public static <T> boolean isEmpty(T[] array) {
        return array == null || array.length == 0;
    }
}
