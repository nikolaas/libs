package org.ns.ioc;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executor;
import org.ns.func.Callback;

/**
 *
 * @author stupak
 */
final class Instancer {
    
    private final ConcurrentMap <Class<?>, Binding<?>> bindings;
    private final BackgroundExecutor executor;

    Instancer() {
        this.bindings = new ConcurrentHashMap<>();
        this.executor = createExecutor();
    }
    
    private BackgroundExecutor createExecutor() {
        return new WrapExecutor(java.util.concurrent.Executors.newFixedThreadPool(3));
    }
    
    public <T> T get(Class<T> iface) {
        if ( iface == null ) {
            throw new NullPointerException("iface is null");
        }
        Binding<T> binding = (Binding<T>) bindings.get(iface);
        if ( binding == null ) {
            throw new IoCException("Instance of " + iface.getName() + " not configured in IoC.");
        }
        return binding.getInstance();
    }
    
    public <T> T getOrNull(Class<T> iface) {
        if ( iface == null ) {
            throw new NullPointerException("iface is null");
        }
        Binding<T> binding = (Binding<T>) bindings.get(iface);
        if ( binding == null ) {
            return null;
        } else {
            return binding.getInstance();
        }
    }
    
    public <T> void waitGet(Class<T> iface, final Callback<T> callback) {
        if ( iface == null ) {
            throw new NullPointerException("iface is null");
        }
        if ( callback == null ) {
            throw new NullPointerException("callback is null");
        }
        final Binding<T> binding = (Binding<T>) bindings.get(iface);
        if ( binding == null ) {
            throw new IoCException("Instance of " + iface.getName() + " not configured in IoC.");
        }
        executor.execute(new Runnable() {

            @Override
            public void run() {
                T instance = binding.getInstance();
                callback.call(instance);
            }
        });
    }
    
    public <T> Binding<T> bind(Factory<T> instanceFactory, Class<T> iface) {
        if ( iface == null ) {
            throw new NullPointerException("iface is null");
        }
        if ( instanceFactory == null ) {
            throw new NullPointerException("instanceFactory is null");
        }
        Binding<T> binding = new Binding<>(this, iface, instanceFactory);
        Binding<T> old = (Binding<T>) bindings.putIfAbsent(iface, binding);
        if ( old != null ) {
            throw new IoCException("Instance of " + iface.getName() + " already configured in IoC.");
        }
        return binding;
    }
    
    public <T> void unbind(Binding<T> binding) {
        bindings.remove(binding.getIface());
    }
    
    private static interface BackgroundExecutor {
        void execute(Runnable r);
    }
    
    private static class WrapExecutor implements BackgroundExecutor {

        private final Executor executor;

        public WrapExecutor(Executor executor) {
            this.executor = executor;
        }
        
        @Override
        public void execute(Runnable r) {
            executor.execute(r);
        }
        
    }
}
