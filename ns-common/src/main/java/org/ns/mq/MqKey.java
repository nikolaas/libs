package org.ns.mq;

import java.util.Objects;

/**
 * Ключ очереди сообщений
 * @param <K> Тип ключа сообщения
 * @param <V> Тип значения сообщения
 * @author stupak
 */
public final class MqKey<K, V> {
    
    private final String id;
    private final  Class<K> keyClass;
    private final Class<V> valueClass;

    MqKey(String id, Class<K> keyClass, Class<V> valueClass) {
        this.id = id;
        this.keyClass = keyClass;
        this.valueClass = valueClass;
    }

    /**
     * Получение идентификатора ключа очереди сообщений
     * @return Идентификатор ключа очереди сообщений
     */
    public String getId() {
        return id;
    }

    /**
     * Получение типа ключа сообщения
     * @return Тип ключа сообщения
     */
    public Class<K> getKeyClass() {
        return keyClass;
    }

    /**
     * Получение типа значения сообщения
     * @return Тип значения сообщения
     */
    public Class<V> getValueClass() {
        return valueClass;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MqKey<?, ?> other = (MqKey<?, ?>) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "MqKey{" + "id=" + id + '}';
    }

    /**
     * Создание ключа очереди сообщений.<br>
     * Вызывает {@link #create(java.lang.String, java.lang.Class, java.lang.Class)} с параметрами
     * keyClass и valueClass равными Object.class.
     * @param <K> Тип ключа сообщения
     * @param <V> Тип значения сообщения
     * @param id Идентификатор ключа очереди сообщений
     * @return Ключ очереди сообщений
     */
    public static <K, V> MqKey<K, V> create(String id) {
        return (MqKey<K, V>) new MqKey<>(id, Object.class, Object.class);
    }
    
    /**
     * Создание ключа очереди сообщений
     * @param <K> Тип ключа сообщения
     * @param <V> Тип значения сообщения
     * @param id Идентификатор ключа очереди сообщений
     * @param keyClass Тип ключа сообщения
     * @param valueClass Тип значения сообщения
     * @return Ключ очереди сообщений
     */
    public static <K, V> MqKey<K, V> create(String id, Class<K> keyClass, Class<V> valueClass) {
        return new MqKey<>(id, keyClass, valueClass);
    }
}
