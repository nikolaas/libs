package org.ns.pojo;

import java.lang.annotation.Annotation;

/**
 *
 * @author Николай
 */
public interface PojoProperty {
    
    String getName();
    Class<?> getType();
    Object getValue(Object instance);
    void setValue(Object instance, Object value);
    
    boolean isReadable();
    boolean isWritable();
    
    <T extends Annotation> T getAnnotation(Class<T> annotationClass);
}
