package org.ns.pojo;

import java.util.List;

/**
 *
 * @param <T>
 * @author Николай
 */
public class Pojo<T> implements PojoModel<T> {
    
    private final PojoClass<T> pojoClass;
    private final T instance;

    Pojo(PojoClass<T> pojoClass, T instance) {
        this.pojoClass = pojoClass;
        this.instance = instance;
    }
    
    @Override
    public int getPropertiesCount() {
        return pojoClass.getPropertiesCount();
    }
    
    @Override
    public PojoProperty getProperty(int index) {
        return pojoClass.getProperty(index);
    }
    
    @Override
    public PojoProperty getProperty(String name) {
        return pojoClass.getProperty(name);
    }
    
    @Override
    public List<PojoProperty> getProperties() {
        return pojoClass.getProperties();
    }
    
    @Override
    public Class<T> getJavaClass() {
        return pojoClass.getJavaClass();
    }
    
    public Object getPropertyValue(int index) {
        PojoProperty prop = getProperty(index);
        return prop.getValue(instance);
    }
    
    public void setPropertyValue(int index, Object value) {
        PojoProperty prop = getProperty(index);
        prop.setValue(instance, value);
    }
}
