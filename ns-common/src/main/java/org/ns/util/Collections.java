package org.ns.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

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
    
    public static <E> Iterable<E> iterable(Enumeration<E> enumeration) {
        return new IteratorWrapper<>(iterator(enumeration));
    }
    
    public static <E> Iterable<E> iterable(E[] array) {
        return new IteratorWrapper(iterator(array));
    }
    
    public static <E> Iterator<E> iterator(Enumeration<E> enumeration) {
        return new EnumerationIterator<>(enumeration);
    }
    
    public static <E> Iterator<E> iterator(E[] array) {
        return new ArrayIterator<>(array);
    }
    
    public static <E> List<E> list(Enumeration<E> enumeration) {
        return list(iterable(enumeration));
    }
    
    public static <E> List<E> list(E[] array) {
        return list(iterable(array));
    }
    
    public static <E> List<E> list(Iterable<E> iterable) {
        List<E> list = new ArrayList<>();
        for ( E e : iterable ) {
            list.add(e);
        }
        return list;
    }
    
    private static class IteratorWrapper<E> implements Iterable<E> {

        private final Iterator<E> iterator;

        public IteratorWrapper(Iterator<E> iterator) {
            this.iterator = iterator;
        }
        
        @Override
        public Iterator<E> iterator() {
            return iterator;
        }
        
    }
    
    private static class ArrayIterator<E> implements Iterator<E> {

        private final E[] array;
        private int current = -1;

        public ArrayIterator(E[] array) {
            this.array = array;
        }
        
        @Override
        public boolean hasNext() {
            return current < array.length - 1;
        }

        @Override
        public E next() {
            if ( !hasNext() ) {
                throw new NoSuchElementException();
            }
            return array[++current];
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Not supported yet.");
        }
        
    }
    
    private static class EnumerationIterator<E> implements Iterator<E> {

        private final Enumeration<E> enumeration;

        public EnumerationIterator(Enumeration<E> enumeration) {
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
