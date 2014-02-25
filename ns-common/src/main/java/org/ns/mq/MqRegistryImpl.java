package org.ns.mq;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Реализация реестра очередей сообщений.
 * @author stupak
 */
public class MqRegistryImpl implements MqRegistry {

    private final ConcurrentMap<MqKey, MessageQueueImpl> queues = new ConcurrentHashMap<>();

    /**
     * Создание реестра очередей сообщений.
     * @param keys Список ключей очередей сообщений, очереди для которых будут созданы по умолчанию
     */
    public MqRegistryImpl(MqKey<?, ?>... keys) {
        if ( keys != null ) {
            for (MqKey<?, ?> key : keys) {
                queues.put(key, new MessageQueueImpl());
            }
        }
    }

    /**
     * Получение очереди сообщений по ключу очереди сообщений
     * @param <K> Тип ключа сообщения
     * @param <V> Тип значения сообщения
     * @param key Ключ очереди сообщений
     * @return Очередь сообщений
     */
    @Override
    public <K, V> MessageQueue<K, V> getMessageQueue(MqKey<K, V> key) {
        return queues.get(key);
    }

    /**
     * Получение отправителя сообщений в очередь по ключу очереди сообщений
     * @param <K> Тип ключа сообщения
     * @param <V> Тип значения сообщения
     * @param key Ключ очереди сообщений
     * @return Отправитель сообщений в очередь
     */
    @Override
    public <K, V> MqSender<K, V> getMqSender(MqKey<K, V> key) {
        return queues.get(key);
    }

    /**
     * Создание очереди сообщений по переданному ключу очереди.<br>
     * Такжу будет создан отправитель сообщений в созданную очередь.
     * @param <K> Тип ключа сообщения
     * @param <V> Тип значения сообщения
     * @param key Ключ очереди сообщений
     * @return Очередь сообщений
     */
    @Override
    public <K, V> MessageQueue createMessageQueue(MqKey<K, V> key) {
        if ( containsMessageQueue(key) ) {
            return getMessageQueue(key);
        }
        MessageQueueImpl<K, V> queue = new MessageQueueImpl<>();
        MessageQueueImpl<K, V> old = (MessageQueueImpl<K, V>) queues.putIfAbsent(key, queue);
        if ( old != null ) {
            queue = old;
        }
        return queue;
    }

    /**
     * Проверка существования очереди сообщений, зарегистрированной на указанный ключ
     * @param <K> Тип ключа сообщения
     * @param <V> Тип значения сообщения
     * @param key Ключ очереди сообщений
     * @return true, если очередь сообщений существует, иначе - false
     */
    @Override
    public <K, V> boolean containsMessageQueue(MqKey<K, V> key) {
        return queues.containsKey(key);
    }
    
}
