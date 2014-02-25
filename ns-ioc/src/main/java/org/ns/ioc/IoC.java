package org.ns.ioc;

import org.ns.func.Callback;

/**
 * Сервис для "склеивания" компонентов приложения и конфигурирования их зависимостей.<br>
 * Представляет из себя контейнер, содержащий связанные пары "интерфейс - реализация". В основе работы сервиса лежит принцип 
 * <a href="http://ru.wikipedia.org/wiki/%D0%98%D0%BD%D0%B2%D0%B5%D1%80%D1%81%D0%B8%D1%8F_%D1%83%D0%BF%D1%80%D0%B0%D0%B2%D0%BB%D0%B5%D0%BD%D0%B8%D1%8F">
 * "Inversion of Control"</a>.<br>
 * Использует интерфейс как ключ для поиска его зарегистрированной реализации. Причем на интерфейс регистрируется 
 * не реализация его (интерфейса), а {@link Factory фабрика реализаций}. Таким образом, в зависимости от 
 * свойств фабрики реализаций возможно различное поведение: как генерация каждый раз новой 
 * реализации интерфейса при обращении к методу {@link #get(java.lang.Class) }, так и возвращение всегда одной 
 * и тоже реализации (паттерн "Одиночка"), или использование особого поведения, например c {@link java.lang.ThreadLocal }.<br>
 * <strong>Отмечу</strong>, что удаление реализации интерфейса осуществляется только путем вызова у
 * объекта {@link  Binding}, возвращаемого при регистрации реализации интерфейса, метода {@link Binding#close() }.
 * @author stupak
 */
public final class IoC {

    private IoC() {
    }
    
    
    private static final Instancer INSTANCER = new Instancer();
    
    /**
     * Получение реализации указаного интерфейса, зарегистрированного в {@link IoC}.<br>
     * Если интерфейс не зарегистрирован в IoC, то будет <strong>брошено исключение</strong>
     * @param <T>
     * @param iface Интерфейс
     * @return Реализация указаного интерфейса
     */
    public static <T> T get(Class<T> iface) {
        return INSTANCER.get(iface);
    }
    
    /**
     * Получение реализации указаного интерфейса, зарегистрированного в {@link IoC}.<br>
     * Если реализация не зарегистрирована в IoC, то будет возвращен <code>null</cpde>.
     * @param <T>
     * @param iface Интерфейс
     * @return Реализация указаного интерфейса
     */
    public static <T> T getOrNull(Class<T> iface) {
        return INSTANCER.getOrNull(iface);
    }
    
    /**
     * Получение реализации указаного интерфейса, зарегистрированного в {@link IoC}.<br>
     * Метод предназначен для получения реализации указанного интерфейса, 
     * инициализация которой представляет из себя длительную задачу.
     * После завершения инициализации реализации, она будет передана в
     * метод {@link org.nikolaas.gma.util.func.Callback#call(java.lang.Object) call}
     * переданного {@link org.nikolaas.gma.util.func.Callback callback} (метод <code>call</code> 
     * будет вызван не из AWT потока).<br>
     * Если переданный интерфейс не зарегистрирован в IoC, то будет <strong>брошено исключение</strong>.
     * @param <T>
     * @param iface Интерфейс
     * @param callback Объект для дальнешей обработки реализации переданного интерфейса
     */
    public static <T> void get(Class<T> iface, Callback<T> callback) {
        INSTANCER.waitGet(iface, callback);
    }
    
    /**
     * Регистрация переданной реализации на переданный интерфейс.<br>
     * Метод использует паттерн "Одиночка" ("Singleton"), и переданный инстанс 
     * регистрируется в виде такого Singleton-объекта
     * @param <T>
     * @param instance Реализация переданного интерфейса
     * @param iface Интерфейс
     * @return Объект позволяющий удалить регистрацию переданной реализации
     */
    public static <T> Binding<T> bind(T instance, Class<T> iface) {
        return bind(Factories.singletonFactory(instance), iface);
    }
    
    /**
     * Регистрация переданной фабрики реализации на переданный интерфейс.
     * @param <T>
     * @param instanceFactory Фабрика реализаций переданного интерфейса
     * @param iface Интерфейс
     * @return Объект позволяющий удалить регистрацию переданной фабрики реализаций
     */
    public static <T> Binding<T> bind(Factory<T> instanceFactory, Class<T> iface) {
        return INSTANCER.bind(instanceFactory, iface);
    }
}
