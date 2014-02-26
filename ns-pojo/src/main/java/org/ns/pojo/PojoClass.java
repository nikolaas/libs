package org.ns.pojo;

import java.util.Collections;
import java.util.List;

/**
 *
 * @param <T>
 * @author Николай
 */
public class PojoClass<T> implements PojoModel<T> {
    
    private final Class<T> javaClass;
    private final List<PojoProperty> properties;

    PojoClass(Class<T> javaClass, List<PojoProperty> properties) {
        this.javaClass = javaClass;
        this.properties = properties;
    }
    
    @Override
    public int getPropertiesCount() {
        return properties.size();
    }
    
    @Override
    public PojoProperty getProperty(int index) {
        return properties.get(index);
    }
    
    @Override
    public PojoProperty getProperty(String name) {
        for (PojoProperty p : properties) {
            if ( p.getName().equals(name) ) {
                return p;
            }
        }
        return null;
    }
    
    @Override
    public List<PojoProperty> getProperties() {
        return Collections.unmodifiableList(properties);
    }
    
    @Override
    public Class<T> getJavaClass() {
        return javaClass;
    }
    
    public Object getPropertyValue(T instance, int index) {
        PojoProperty prop = getProperty(index);
        return prop.getValue(instance);
    }
    
    public void setPropertyValue(T instance, int index, Object value) {
        PojoProperty prop = getProperty(index);
        prop.setValue(instance, value);
    }
    
}
