package org.ns.util;

/**
 *
 * @author stupak
 */
public interface Closeable extends java.io.Closeable{
    
    @Override
    void close();
    
}
