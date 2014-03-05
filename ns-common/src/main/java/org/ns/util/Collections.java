package org.ns.util;

import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
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
    
    public static<E> Iterator<E> iterator(Enumeration<E> enumeration) {
        return new EnumarationIterator<>(enumeration);
    }
    
    private static class EnumarationIterator<E> implements Iterator<E> {

        private final Enumeration<E> enumeration;

        public EnumarationIterator(Enumeration<E> enumeration) {
            this.enumeration = enumeration;
        }
        
        @Override
        public boolean hasNext() {
            return enumeration.hasMoreElements();
        }

        @Override
        public E next() {
            return enumeration.nextElement();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Not supported yet.");
        }
        
    }
}
