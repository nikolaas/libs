package org.ns.pojo;

import java.util.List;

/**
 *
 * @param <T>
 * @author Николай
 */
public interface PojoModel<T> {

    int getPropertiesCount();

    PojoProperty getProperty(int index);

    PojoProperty getProperty(String name);

    List<PojoProperty> getProperties();

    Class<T> getJavaClass();
    
}
