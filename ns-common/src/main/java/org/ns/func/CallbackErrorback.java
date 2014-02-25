package org.ns.func;

/**
 *
 * @author stupak
 */
public class CallbackErrorback<T> implements Callback<T>, Errorback {

    private final Callback<T> callback;
    private final Errorback errorback;

    public CallbackErrorback(Callback<T> callback, Errorback errorback) {
        this.callback = callback;
        this.errorback = errorback;
    }
    
    @Override
    public void call(T arg) {
        if ( callback != null ) {
            callback.call(arg);
        }
    }

    @Override
    public void error(Throwable error) {
        if ( errorback != null ) {
            errorback.error(error);
        }
    }
    
}
