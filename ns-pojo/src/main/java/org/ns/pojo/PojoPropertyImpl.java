package org.ns.pojo;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 *
 * @author Николай
 */
class PojoPropertyImpl implements PojoProperty {

    private final String name;
    private final Method getter;
    private final Method setter;

    public PojoPropertyImpl(String name, Method getter, Method setter) {
        this.name = name;
        this.getter = getter;
        this.setter = setter;
    }

    @Override
    public String getName() {
        return name;
    }
    
    @Override
    public Object getValue(Object instance) {
        if ( !isReadable()) {
            throw new PojoException("Property " + name + " is not readable.");
        }
        Object value = null;
        try {
            value = getter.invoke(instance);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            throw new PojoException("Exception was occured then to read property " + name, ex);
        }
        return value;
    }

    @Override
    public void setValue(Object instance, Object value) {
        if ( !isWritable() ) {
            throw new PojoException("Property " + name + " is not writable.");
        }
        try {
            setter.invoke(instance, value);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            throw new PojoException("Exception was occured then to write property " + name, ex);
        }
    }

    @Override
    public boolean isReadable() {
        return getter != null;
    }

    @Override
    public boolean isWritable() {
        return setter != null;
    }
    
}
