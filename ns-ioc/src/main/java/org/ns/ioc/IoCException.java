package org.ns.ioc;

/**
 *
 * @author stupak
 */
public class IoCException extends RuntimeException {

    public IoCException() {
        super();
    }
    
    public IoCException(String detailMessage) {
        super(detailMessage);
    }
    
    public IoCException(String detailMessage, Throwable t) {
        super(detailMessage, t);
    }
    
    public IoCException(Throwable t) {
        super(t);
    }
    
}
