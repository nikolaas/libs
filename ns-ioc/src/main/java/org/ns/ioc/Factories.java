package org.ns.ioc;

/**
 * 
 * @author stupak
 */
public final class Factories {

    private Factories() {
    }
    
    private static class SingletonFactory<T> implements Factory<T> {

        private final T instance;

        public SingletonFactory(T instance) {
            this.instance = instance;
        }
        
        @Override
        public T createInstance() throws Exception {
            return instance;
        }
        
    }
    
    private static final class LazySingletonFactory<T> implements Factory<T> {

        private final Class<?> instanceClass;
        private T instance;

        public LazySingletonFactory(Class<? extends T> instanceClass) {
            this.instanceClass = instanceClass;
            this.instance = null;
        }
        
        @Override
        public T createInstance() throws Exception {
            if ( instance == null ) {
                instance = (T) instanceClass.newInstance();
            }
            return instance;
        }
        
    }
    
    /**
     * Фабрика одиночек, всегда возвращает одну и ту же (переданную ей при создании) реализацию интерфейса
     * @param <T>
     * @param instance Реализация интерфейса
     * @return Фабрика одиночек
     */
    public static <T> Factory<T> singletonFactory(T instance) {
        return new SingletonFactory<>(instance);
    }
    
    /**
     * Фабрика одиночек с отложенной загрузкой, всегда возвращает одну и ту же реализацию интерфейса.<br>
     * При создании фабрики, ей передается класс-реализация интерфейса. При первом вызове метода
     * {@link Factory#createInstance() } производится инициализация реализации интерфейса. 
     * При последующих вызовах метода {@link Factory#createInstance()} возвращается 
     * созданная при первом вызове реализация интерфейса.<br>
     * <strong>Внимание!</strong> Поскольку реализация интерфейса создается с помощью метода {@link Class#newInstance()},
     * то класс, представляющий реализацию, должен иметь конструктор по умолчанию.
     * @param <T>
     * @param instanceClass Класс, представляющий реализацию интерфейса
     * @return Фабрика одиночек с отложенной загрузкой
     */
    public static <T> Factory<T> lazySigletonFactory(Class<? extends T> instanceClass) {
        return new LazySingletonFactory<>(instanceClass);
    }
}
