package org.ns.util;

/**
 *
 * @author stupak
 */
public interface Matcher<E> {
    boolean match(E e1, E e2);
}
