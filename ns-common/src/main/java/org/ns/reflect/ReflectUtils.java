package org.ns.reflect;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author stupak
 */
public class ReflectUtils {

    private static final Map<Class<?>, Class<?>> PRIMITIVE_BOX = new HashMap<>();
    static {
        PRIMITIVE_BOX.put(byte.class, Byte.class);
        PRIMITIVE_BOX.put(short.class, Short.class);
        PRIMITIVE_BOX.put(int.class, Integer.class);
        PRIMITIVE_BOX.put(long.class, Long.class);
        PRIMITIVE_BOX.put(float.class, Float.class);
        PRIMITIVE_BOX.put(double.class, Double.class);
        PRIMITIVE_BOX.put(boolean.class, Boolean.class);
    }
    
    private ReflectUtils() {
    }

    public static Class<?> wrapPrimitive(Class<?> primitive) {
        return PRIMITIVE_BOX.get(primitive);
    }
}
