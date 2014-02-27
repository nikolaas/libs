package org.ns.reflect;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author stupak
 */
public class TypeConverter {

    public static TypeConverter defaultConverter() {
        TypeConverter converter = new TypeConverter()
                .register(new NumberHandler());
        return converter;
    }
    
    public static interface TypeHandler {
        Object convert(Context context);
        Class<?> getTargetType();
        Class<?> getSourceClass();
    }
    
    public static class Context {
        private final Class<?> targetClass;
        private final Class<?> sourceClass;
        private final Object value;

        public Context(Class<?> targetClass, Class<?> sourceClass, Object value) {
            this.targetClass = targetClass;
            this.sourceClass = sourceClass;
            this.value = value;
        }

        public Class<?> getSourceClass() {
            return sourceClass;
        }

        public Class<?> getTargetClass() {
            return targetClass;
        }

        public Object getValue() {
            return value;
        }
        
    }
    
    private final Map<Class<?>, TypeHandler> handlerByTarget;

    public TypeConverter() {
        this.handlerByTarget = new HashMap<>();
    }
    
    public TypeConverter register(TypeHandler handler) {
        handlerByTarget.put(handler.getTargetType(), handler);
        return this;
    }
    
    public Object convert(Class<?> targetClass, Object value) {
        if ( targetClass.isInstance(value) ) {
            return value;
        }
        Class<?> valueClass = value.getClass();
        Class<?> sourceClass;
        if ( valueClass.isPrimitive() ) {
            sourceClass = ReflectUtils.wrapPrimitive(valueClass);
        } else {
            sourceClass = valueClass;
        }
        TypeHandler handler = findHandler(sourceClass, targetClass);
        if ( handler != null ) {
            return handler.convert(new Context(targetClass, sourceClass, value));
        } else {
            throw new RuntimeException("TypeHandler not found: can't convert " + sourceClass.getName() + " to " + targetClass.getName());
        }
    }
    
    private TypeHandler findHandler(Class<?> sourceClass, Class<?> targetClass) {
        return ReflectUtils.findHandler(sourceClass, handlerByTarget);
    }
    
    private static class NumberHandler implements TypeHandler {

        @Override
        public Object convert(Context context) {
            Number value = (Number) context.getValue();
            Class<?> targetClass = context.getTargetClass();
            if ( targetClass == byte.class ) {
                return value.byteValue();
            } else if ( targetClass == short.class ) {
                return value.shortValue();
            } else if ( targetClass == int.class ) {
                return value.intValue();
            } else if ( targetClass == long.class ) {
                return value.longValue();
            } else if ( targetClass == float.class ) {
                return value.floatValue();
            } else if ( targetClass == double.class ) {
                return value.doubleValue();
            } else {
                return null;
            }
        }

        @Override
        public Class<?> getTargetType() {
            return Number.class;
        }

        @Override
        public Class<?> getSourceClass() {
            return Number.class;
        }
        
    }
}
