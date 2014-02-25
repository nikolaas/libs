package org.ns.mq;

import org.ns.event.ConsumeableEvent;

/**
 * Сообщение для очереди сообщения
 * @param <K> Тип ключа сообщения
 * @param <V> Тип значения сообщения
 * @author stupak
 */
public class MqEvent<K, V> extends ConsumeableEvent<MqSender> {
   
    private final Object initiator;
    private final K key;
    private final V value;

    public MqEvent(MqSender source, Object initiator, K key, V value) {
        super(source);
        this.initiator = initiator;
        this.key = key;
        this.value = value;
    }

    /**
     * Получение инициатора сообщения
     * @return Инициатора сообщения
     */
    public Object getInitiator() {
        return initiator;
    }

    /**
     * Получение ключа сообщения
     * @return Ключ сообщения
     */
    public K getKey() {
        return key;
    }

    /**
     * Получение значения сообщения
     * @return Значение сообщения
     */
    public V getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "MqEvent{" + "key=" + key + ", value=" + value + '}';
    }
    
}
