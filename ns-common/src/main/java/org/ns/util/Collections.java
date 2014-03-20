package org.ns.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import org.ns.func.Function;

/**
 *
 * @author stupak
 */
public class Collections {

    private Collections() {
    }
    
    private static class ToStringFunction<E> implements Function<E, String> {

        @Override
        public String apply(E key) {
            return key.toString();
        }
        
    }
    
    private static final Matcher<Object> LINK_EQUALS_MATCHER = new Matcher<Object>() {

        @Override
        public boolean match(Object e1, Object e2) {
            return e1 == e2;
        }
    };
    
    public static <E> boolean contains(E[] array, E element) {
        return indexOf(array, element) > -1;
    }
    
    public static <E> boolean contains(E[] array, E element, Matcher<E> matcher) {
        return indexOf(array, element, matcher) > -1;
    }
    
    public static <E> int indexOf(E[] array, E element) {
        return indexOf(array, element, LINK_EQUALS_MATCHER);
    }
    
    public static <E> int indexOf(E[] array, E element, Matcher<E> matcher) {
        Iterator<E> iterator = iterator(array);
        int index = 0;
        while ( iterator.hasNext() ) {
            E current = iterator.next();
            if ( matcher.match(current, element) ) {
                return index;
            } else {
                index++;
            }
        }
        return -1;
    }
    
    public static <E> String join(Collection<E> collection, String delimiter) {
        return join(collection, delimiter);
    }
    
    public static <E> String join(E[] array, String delimiter) {
        return join(iterable(array), delimiter);
    }
    
    public static <E> String join(Enumeration<E> enumeration, String delimiter) {
        return join(iterable(enumeration), delimiter);
    }
    
    public static <E> String join(Iterator<E> iterator, String delimiter) {
        return join(new IteratorWrapper<>(iterator), delimiter);
    }
    
    public static <E extends Object> String join(Iterable<E> iterable, String delimiter) {
        return join(iterable, delimiter, new ToStringFunction<E>());
    }
    
    public static <E extends Object> String join(Iterable<E> iterable, String delimiter, Function<E, String> stringConverter) {
        StringBuilder builder = new StringBuilder();
        Iterator<E> iterator = iterable.iterator();
        while ( iterator.hasNext() ) {
            E element = iterator.next();
            builder.append(stringConverter.apply(element));
            if ( iterator.hasNext() ) {
                builder.append(delimiter);
            }
        }
        return builder.toString();
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
