package org.ns.event;

/**
 *
 * @author stupak
 */
public interface Consumeable {

    boolean isConsumed();
    
    void consume();
}
