package org.ns.func;

/**
 *
 * @author stupak
 * @param <V>
 */
public interface Callable<V> {
    V call() throws Exception;
}
