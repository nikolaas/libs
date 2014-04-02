package org.ns.gui.selector;

import java.awt.Color;
import java.awt.Component;

/**
 *
 * @author stupak
 */
public class ValueSetters {

    private ValueSetters() {
    }
    
    public static final ValueSetter<Color> BACKGROUND_SETTER = new ValueSetter<Color>() {

        @Override
        public void setValue(Color value, Component component) {
            component.setBackground(value);
        }
    };
}
