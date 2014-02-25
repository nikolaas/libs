package org.ns.event;

/**
 *
 * @author stupak
 */
public interface Listener<E> {
    
    void listen(E event);
}
