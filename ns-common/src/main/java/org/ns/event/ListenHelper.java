package org.ns.event;

import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author stupak
 * @param <E>
 */
public class ListenHelper<E> implements Iterable<Listener<E>>, Listener<E>{
    
    private static final Logger log = Logger.getLogger(ListenHelper.class.getName());
    private boolean fired;
    private final Listeners<E> listeners;

    public ListenHelper() {
        listeners = new Listeners<>();
    }
    
    public boolean add(Listener<E> l) {
        return listeners.add(l);
    }
    
    public void remove(Listener<E> l) {
        listeners.remove(l);
    }
    
    public boolean contains(Listener<E> l) {
        return listeners.contains(l);
    }

    public void clear() {
        listeners.clear();
    }
    
    @Override
    public Iterator<Listener<E>> iterator() {
        return listeners.iterator();
    }

    @Override
    public void listen(E event) {
        fired = true;
        try {
            for ( Listener<E> l : listeners ) {
                try {
                    l.listen(event);
                    if ( isConsumed(event) ) {
                        break;
                    }
                } catch (Throwable t) {
                    log.log(Level.SEVERE, "Listener occurs error:", t);
                }
            }
        } finally {
            fired = false;
        }
    }

    public boolean isFired() {
        return fired;
    }
    
    public boolean isEmpty() {
        return listeners.isEmpty();
    }
    
    private static boolean isConsumed(Object o) {
         return o instanceof Consumeable && ((Consumeable) o).isConsumed();
    }
    
    public static void comsume(Consumeable consumeable) {
        consumeable.consume();
    }
}
