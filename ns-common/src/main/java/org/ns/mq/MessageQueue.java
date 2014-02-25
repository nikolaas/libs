package org.ns.mq;

import org.ns.event.Listener;

/**
 * Очередь сообщений
 * @param <K> Тип ключа сообщения
 * @param <V> Тип значения сообщения
 * @author stupak
 */
public interface MessageQueue<K, V> {
    
    /**
     * Подписка слушателя на сообщения с указанным ключом.<br>
     * Если в качестве ключа указан <code>null</code>, то слушатель будет получать все сообщения очереди.
     * @param key Ключ сообщения
     * @param l Слушатель сообщений
     */
    void subscribe(K key, Listener<MqEvent<K, V>> l);
    
    /**
     * Удаление слушателя из очереди, подписанного на события с указанынм ключом.
     * @param key Ключ сообщения
     * @param l Слушатель сообщений
     */
    void unsubscribe(K key, Listener<MqEvent<K, V>> l);
    
}
