package org.ns.pojo;

/**
 *
 * @author Николай
 */
public final class PojoManager {
    
    private static final PojoManager INSTANCE = new PojoManager();
    
    public static PojoManager getInstace() {
        return INSTANCE;
    }
    
    private PojoManager() {
    }
    
    public <T> PojoClass<T> createPojoClass(Class<T> javaClass) {
        return createPojoClass(javaClass, null);
    }
    
    public <T> PojoClass<T> createPojoClass(Class<T> javaClass, PojoFilter filter) {
        return new PojoClassBuilder<T>()
                .javaClass(javaClass)
                .filter(filter)
                .build();
    }
    
    public <T> Pojo<T> createPojo(T instance) {
        return createPojo(instance, null);
    }
    
    public <T> Pojo<T> createPojo(T instance, PojoFilter filter) {
        Class<T> javaClass = (Class<T>) instance.getClass();
        PojoClass<T> pojoClass = createPojoClass(javaClass, filter);
        return new Pojo<>(pojoClass, instance);
    }
}
