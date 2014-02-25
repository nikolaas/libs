package org.ns.func;

import java.util.Map;

/**
 *
 * @author stupak
 */
public class Functions {

    private Functions() {
    }
    
    private static class SingletonFunction<K, V> implements Function<K, V> {

        private final V value;
        
        public SingletonFunction(V value) {
            this.value = value;
        }
        
        @Override
        public V apply(K key) {
            return value;
        }
        
    }
    
    private static class KeyFunction<K> implements Function<K, K> {

        @Override
        public K apply(K key) {
            return key;
        }
        
    }
    
    private static class MapFunction<K, V> implements Function<K, V> {

        private final Map<K, V> map;

        public MapFunction(Map<K, V> map) {
            this.map = map;
        }
        
        @Override
        public V apply(K key) {
            return map.get(key);
        }
        
    }
    
    public static <K, V> Function<K, V> singletonFunction(V value) {
        return (Function<K, V>) new SingletonFunction<>(value);
    }
    
    public static <K> Function<K, K> keyFunction() {
        return new KeyFunction<>();
    }
    
    public static <K, V> Function<K, V> mapFunction(Map<K, V> map) {
        return new MapFunction<>(map);
    }
}
