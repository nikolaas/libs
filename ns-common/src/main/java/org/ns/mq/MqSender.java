package org.ns.mq;

/**
 * Отправитель сообщений в очередь
 * @param <K> Тип ключа сообщения
 * @param <V> Тип значения сообщения
 * @author stupak
 */
public interface MqSender<K, V> {
    
    /**
     * Отправка сообщения в очередь (с последующей рассылкой его всем подписанным на него слушателям)
     * @param initiator Инициатор сообщения
     * @param key Ключ сообщения
     * @param value Значение сообщения
     */
    void send(Object initiator, K key, V value);
    
}
