package org.ns.func;

/**
 *
 * @author stupak
 * @param <K>
 * @param <V>
 */
public interface Function<K, V> extends UnsafeFunction<K, V, RuntimeException> {
    @Override
    V apply(K key);
}
