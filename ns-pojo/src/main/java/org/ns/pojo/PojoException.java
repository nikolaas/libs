package org.ns.pojo;

/**
 *
 * @author Николай
 */
public class PojoException extends RuntimeException {

    public PojoException() {
        super();
    }
    
    public PojoException(String message) {
        super(message);
    }
    
    public PojoException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public PojoException(Throwable cause) {
        super(cause);
    }
}
