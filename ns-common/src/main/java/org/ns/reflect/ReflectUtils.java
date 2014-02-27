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
    
      /**
   * Поднимается по иерархии классов и реализуемых интерфейсов указанного класса
   * пока не найдет для хотябы одного из них соответсвие в карте.
   * @title Поиск классов, от который наследован текущий класс, и реализуемых интерфейсов для текущего класса в карте соответствий
   * @param <C>
   * @param <V>
   * @param clazz класс для котрого нужно найти соответствие
   * @param handlers карта соответствий значений классам и интерфейсам
   * @return Значние найденного в карте соответствия или null
   */
  public static <C, V> V findHandler(Class<C> clazz, Map<Class<?>, V> handlers) {
    V handler = innerFindHandler(clazz, handlers);
    if(handler == null) {
      //defaut handler
      handler = handlers.get(Object.class);
    }
    return handler;
  }
  
  private static <C, V> V innerFindHandler(Class<C> clazz, Map<Class<?>, V> handlers) {
    Class<?> sc = clazz;
    V handler = null;
    //поиск хендлера
    while(sc != null && handler == null) {
      handler = handlers.get(sc);
      if(handler == null) {
        //проверяем сначала интерфейсы
        Class<?> ifaces[] = sc.getInterfaces();
        for(Class<?> iface : ifaces) {
          handler = handlers.get(iface);
          if(handler != null) {
            return handler;
          }
        }
        //затем интерфейсы от которых они наследованы
        for(Class<?> iface : ifaces) {
          for(Class<?> superIface: iface.getInterfaces()) {
            handler = innerFindHandler(superIface, handlers);
            if(handler != null) {
              return handler;
            }
          }
        }
      }
      sc = sc.getSuperclass();
    }
    return handler;
  }
  
}
