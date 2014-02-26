package org.ns.pojo;

import java.lang.reflect.Method;

/**
 *
 * @author Николай
 */
public interface PojoFilter {
    
    boolean filter(Method method);
}
