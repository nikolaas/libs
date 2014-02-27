package org.ns.pojo;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Николай
 */
class PojoClassBuilder<T> {

    private static final String GET = "get";
    private static final String IS = "is";
    private static final String SET = "set";
    
    private Class<T> javaClass;
    private PojoFilter filter;

    public PojoClassBuilder() {
    }
    
    public PojoClassBuilder<T> javaClass(Class<T> javaClass) {
        this.javaClass = javaClass;
        return this;
    }
    
    public PojoClassBuilder<T> filter(PojoFilter filter) {
        this.filter = filter;
        return this;
    }
    
    public PojoClass<T> build() {
        if ( javaClass == null ) {
            throw new NullPointerException("javaClass is null");
        }
        Map<String, PropDesc> propDescs = new HashMap<>();
        Method methods[] = javaClass.getDeclaredMethods();
        for (Method method : methods) {
            if ( filter != null && !filter.filter(method) ) {
                continue;
            }
            if ( isGetter(method) ) {
                String propName = getPropertyName(method);
                PropDesc pd = propDescs.get(propName);
                if ( pd == null ) {
                    pd = new PropDesc();
                    pd.name = propName;
                    pd.getter = method;
                    propDescs.put(propName, pd);
                } else {
                    if ( pd.getter != null ) {
                        throw new PojoException("Class " + javaClass.getName() + " contains many getter methods with name " + propName);
                    }
                    pd.getter = method;
                }
            } else if ( isSetter(method) ) {
                String propName = getPropertyName(method);
                PropDesc pd = propDescs.get(propName);
                if ( pd == null ) {
                    pd = new PropDesc();
                    pd.name = propName;
                    pd.setter = method;
                    propDescs.put(propName, pd);
                } else {
                    if ( pd.setter != null ) {
                        throw new PojoException("Class " + javaClass.getName() + " contains many setter methods with name " + propName);
                    }
                    pd.setter = method;
                }
            }
        }
        List<PojoProperty> props = new ArrayList<>();
        for( PropDesc pd : propDescs.values() ) {
            props.add(new PojoPropertyImpl(pd.name, pd.getter, pd.setter));
        }
        return new PojoClass<>(javaClass, props);
    }
    
    private static class PropDesc {
        private String name;
        private Method getter;
        private Method setter;
    }
    
    private boolean isGetter(Method method) {
        if ( !isGetterName(method) ) {//должен начинаться с get или is
            return false;
        }
        if ( method.getReturnType().isAssignableFrom(Void.class) ) {//должен возвращать значение
            return false;
        }
        if ( method.getParameterTypes().length > 0 ) {//не должен принимать параметры
            return false;
        }
        return true;
    }
    
    private boolean isSetter(Method method) {
        if ( !isSetterName(method) ) {//должен начинаться с set
            return false;
        }
        if ( method.getReturnType().isAssignableFrom(Void.class) ) {//должен быть void
            return false;
        }
        if ( method.getParameterTypes().length != 1 ) {//должен принимать один параметр
            return false;
        }
        return true;
    }
    
    private boolean isGetterName(Method method) {
        String name = method.getName();
        return name.startsWith(GET) || name.startsWith(IS);
    }
    
    private boolean isSetterName(Method method) {
        String name = method.getName();
        return name.startsWith(SET);
    }
    
    private String getPropertyName(Method method) {
        String name = method.getName();
        if ( name.startsWith(GET) ) {
            return firstSymbolToLowerCase(name.substring(GET.length()));
        } else if ( name.startsWith(IS) ) {
            return firstSymbolToLowerCase(name.substring(IS.length()));
        } else if ( name.startsWith(SET) ) {
            return firstSymbolToLowerCase(name.substring(SET.length()));
        } else {
            return null;
        }
    }
    
    private String firstSymbolToLowerCase(String name) {
        return name.substring(0, 1).toLowerCase() + name.substring(1);
    }
}
