package org.ns.event;

/**
 *
 * @author stupak
 */
public class Event<S> {
    
    private final S source;

    public Event(S source) {
        this.source = source;
    }

    public S getSource() {
        return source;
    }
    
}
