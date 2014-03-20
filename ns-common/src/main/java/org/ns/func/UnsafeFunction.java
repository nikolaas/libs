package org.ns.func;

/**
 *
 * @author stupak
 */
public interface UnsafeFunction<K, V, T extends Throwable> {
    public V apply(K key) throws T;
}
