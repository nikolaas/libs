package org.ns.func;

/**
 *
 * @author stupak
 * @param <K>
 * @param <V>
 */
public interface Function<K, V> {
    V apply(K key);
}
