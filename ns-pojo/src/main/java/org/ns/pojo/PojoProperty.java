package org.ns.pojo;

/**
 *
 * @author Николай
 */
public interface PojoProperty {
    
    String getName();
    Object getValue(Object instance);
    void setValue(Object instance, Object value);
    
    boolean isReadable();
    boolean isWritable();
}
