package org.ns.mq;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.ns.util.Assert;
import org.ns.event.ListenHelper;
import org.ns.event.Listener;

/**
 * Реализация очереди сообщений. Также является реализацией и отправителя сообщений в очередь.<br>
 * Класс потокобезопасен.
 * @param <K> Тип ключа сообщения
 * @param <V> Тип значения сообщения
 * @author stupak
 */
class MessageQueueImpl<K, V> implements MessageQueue<K, V>, MqSender<K, V> {

    /**
     * Потокобезопасный список слушателей сообщений очереди.<br>
     * При создании списка указывается ключ сообщений, которые будет рассылать 
     * данный список слушателям
     */
    private class Listeners {
        
        private final Lock lock = new ReentrantLock();
        private final ListenHelper<MqEvent<K, V>> listeners = new ListenHelper<>();
        private final K key;

        public Listeners(K key) {
            Assert.isNotNull(key, "Null keys is not allowed!");
            this.key = key;
        }
        
        /**
         * Рассылка сообщения слушателям.
         * @param event Сообщение для рассылки
         */
        public void fire(MqEvent<K, V> event) {
            if ( !lock.tryLock() ) {
                lock.lock();
            }
            try {
                listeners.listen(event);
            } finally {
                lock.unlock();
            }
        }
        
        /**
         * Добавление слушателя сообщений очереди в список
         * @param l Слушатель сообщений очереди
         */
        public void add(Listener<MqEvent<K, V>> l) {
            lock.lock();
            try {
                listeners.add(l);
            } finally {
                lock.unlock();
            }
        }
        
        /**
         * Удаление слушателя сообщений очереди в список
         * @param l Слушатель сообщений очереди
         */
        public void remove(Listener<MqEvent<K, V>> l) {
            lock.lock();
            try {
                listeners.remove(l);
                if ( listeners.isEmpty() ) {
                    map.remove(key);
                }
            } finally {
                lock.unlock();
            }
        }
    }
    
    private final K NULL = (K) new Object();
    private final ConcurrentMap<K, Listeners> map = new ConcurrentHashMap<>();

    MessageQueueImpl() {
    }

    /**
     * Отправка сообщения в очередь (с последующей рассылкой его всем подписанным на него слушателям).<br>
     * При выполнении метод блокирует список слушателей.
     * @param initiator Инициатор сообщения
     * @param key Ключ сообщения
     * @param value Значение сообщения
     */
    @Override
    public void send(Object initiator, K key, V value) {
        final MqEvent<K, V> event = new MqEvent<>(this, initiator, key, value);
        
        Listeners nullListeners = map.get(NULL);
        if ( nullListeners != null ) {// подписчики с нулевым ключем получают все события
            nullListeners.fire(event);
        }
        
        key = translateKey(key);
        if ( key == NULL ) {//уже разослали выше, второй раз не нужно
            return;
        }
        
        Listeners keyListeners = map.get(key);
        if ( keyListeners != null ) {
            keyListeners.fire(event);
        }
    }

    /**
     * Подписка слушателя на сообщения с указанным ключом.<br>
     * Если в качестве ключа указан <code>null</code>, то слушатель будет получать все сообщения очереди.<br>
     * При выполнении метод блокирует список слушателей.
     * @param key Ключ сообщения
     * @param l Слушатель сообщений
     */
    @Override
    public void subscribe(K key, Listener<MqEvent<K, V>> l) {
        Listeners listeners;
        key = translateKey(key);
        if ( map.containsKey(key) ) {
            listeners = map.get(key);
        } else {
            listeners = new Listeners(key);
            Listeners old = map.putIfAbsent(key, listeners);
            if ( old != null ) {
                listeners = old;
            }
        }
        listeners.add(l);
    }

    /**
     * Удаление слушателя из очереди, подписанного на события с указанынм ключом.<br>
     * При выполнении метод блокирует список слушателей.
     * @param key Ключ сообщения
     * @param l Слушатель сообщений
     */
    @Override
    public void unsubscribe(K key, Listener<MqEvent<K, V>> l) {
        Listeners listeners = map.get(translateKey(key));
        if ( listeners == null ) {
            return;
        }
        listeners.remove(l);
    }

    /**
     * Для нулевых ключей сообщений заведен специальный ключ. Здесь производится 
     * проверка, является ли ключ нулевым, и если это так то возвращается это 
     * специальный ключ
     * @param key Ключ сообщения
     * @return Ключ сообщения или специальный ключ для нулевых ключей сообщений
     */
    private K translateKey(K key) {
        if ( key == null ) {
            return NULL;
        }
        return key;
    }
}
