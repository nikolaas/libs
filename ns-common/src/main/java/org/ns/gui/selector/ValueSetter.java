package org.ns.gui.selector;

import java.awt.Component;

/**
 *
 * @author stupak
 */
public interface ValueSetter<V> {
    void setValue(V value, Component component);
}
