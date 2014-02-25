package org.ns.func;

/**
 * @param <T>
 * @author stupak
 */
public interface Callback<T> {
    
    void call(T arg);
}
