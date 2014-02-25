package org.ns.ioc;

/**
 * Фабрика реализации интерфейса
 * @author stupak
 */
public interface Factory<T> {

    /**
     * Получение реализации интерфейса
     * @return Реализация интерфейса
     * @throws Exception Ошибки, возникающие при инициализации реализации интерфейса
     */
    T createInstance() throws Exception;
}
