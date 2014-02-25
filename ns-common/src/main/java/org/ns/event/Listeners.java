package org.ns.event;

import java.util.Iterator;

/**
 *
 * @author stupak
 */
public class Listeners<E> implements Iterable<Listener<E>> {
    
    private static final int INITIAL_SIZE=5;
    private static final int INCREMENT_SIZE=3;
    private Object listeners[];
    private int count;

    public Listeners() {
        count = 0;
        listeners = new Listener[INITIAL_SIZE];
    }
  
    public boolean add(Listener<E> l) {
        if ( contains(l) ) {
            return false;
        }
        if ( count >= listeners.length ) {
            Object temp[] = new Object[count + INCREMENT_SIZE];
            System.arraycopy(listeners, 0, temp, 0, count);
            listeners = temp;
        }
        listeners[count++] = l;
        return true;
    }
    
    public void remove(Listener<E> l) {
        if ( l == null ) {
            return;
        }
        for (int i = 0; i < listeners.length; i++) {
            if ( listeners[i] == l ) {
                remove(i);
            }
        }
    }
    
    public void remove(int i) {
        checkIndex(i);
        int last = listeners.length - 1;
        if ( i < last ) {
            System.arraycopy(listeners, i + 1, listeners, i, last - i);
        }
        listeners[last] = null;
        --count;
    }
    
    public Listener<E> get(int i) {
        checkIndex(i);
        return (Listener<E>) listeners[i];
    }
    
    private void checkIndex(int i) {
        if ( i < 0 && i > count ) {
            throw new IndexOutOfBoundsException("i = " + i + ", count = " + count);
        }
    }
    
    public boolean contains(Listener<E> l) {
        for (int i = 0; i < listeners.length; i++) {
            if ( listeners[i] == l ) {
                return true;
            }
        }
        return false;
    }

    public void clear() {
        for(int i = 0; i < count; ++i){
            listeners[i] = null;
        }
        count = 0;
    }
    
    @Override
    public Iterator<Listener<E>> iterator() {
        return new Iterator<Listener<E>>() {

            @Override
            public boolean hasNext() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public Listener<E> next() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        };
    }
    
    public boolean isEmpty() {
        return count == 0;
    }
    
    public int size() {
        return count;
    }
    
}
