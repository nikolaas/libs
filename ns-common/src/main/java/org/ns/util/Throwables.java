package org.ns.util;

/**
 *
 * @author stupak
 */
public final class Throwables {

    private Throwables() {
    }

    public static <T extends Exception> void throwIfPosible(Throwable throwable, Class<T> exceptionClass) throws T {
        if ( exceptionClass.isInstance(throwable) ) {
            throw exceptionClass.cast(throwable);
        }
    }
}
