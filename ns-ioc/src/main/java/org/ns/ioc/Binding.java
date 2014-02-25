package org.ns.ioc;

import org.ns.util.Closeable;

/**
 *
 * @author stupak
 */
public class Binding<T> implements Closeable {

    private final Instancer instancer;
    private final Class<T> iface;
    private final Factory<T> instanceFactory;

    Binding(Instancer instancer, Class<T> iface, Factory<T> instanceFactory) {
        this.instancer = instancer;
        this.iface = iface;
        this.instanceFactory = instanceFactory;
    }

    Class<T> getIface() {
        return iface;
    }
    
    T getInstance() {
        T instance = null;
        try {
            T inst = instanceFactory.createInstance();
            instance = iface.cast(inst);
        } catch (Exception ex) {
            throw new IoCException("Instantiation failed:", ex);
        }
        return instance;
    }
    
    @Override
    public void close() {
        instancer.unbind(this);
    }
    
}
