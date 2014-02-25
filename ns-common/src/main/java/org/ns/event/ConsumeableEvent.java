package org.ns.event;

/**
 *
 * @author stupak
 */
public class ConsumeableEvent<S> extends Event<S> implements Consumeable {

    private boolean consume;
    public ConsumeableEvent(S source) {
        super(source);
        consume = false;
    }
    
    @Override
    public boolean isConsumed() {
        return consume;
    }

    @Override
    public void consume() {
        this.consume = true;
    }
}
