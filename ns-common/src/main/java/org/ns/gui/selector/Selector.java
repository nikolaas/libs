package org.ns.gui.selector;

import java.awt.Component;
import java.awt.ItemSelectable;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

/**
 *
 * @param <V>
 * 
 * @author stupak
 */
public class Selector<V> {
    
    public static final int DEFAULT = 0;
    public static final int FOCUSED = 1;
    public static final int SELECTED = 2;
    public static final int ENTERED = 4;
    public static final int PRESSED = 8;
    
    private final Object STUB = new Object();
    private final StateProvider stateProvider = new StateProvider();
    
    private final ValueSetter<V> setter;
    private final Map<Component, Object> components;
    private final Map<Integer, V> stateValues;
    private int state;

    public Selector(V defaultValue, ValueSetter<V> setter) {
        this.components = new WeakHashMap<>();
        this.stateValues = new HashMap<>();
        this.stateValues.put(DEFAULT, defaultValue);
        this.state = DEFAULT;
        this.setter = setter;
    }

    public Selector<V> configure(int state, V value) {
        stateValues.put(state, value);
        return this;
    }
    
    public int getState() {
        return state;
    }

    private void onState(int state) {
        setState(this.state | state);
    }
    
    private void offState(int state) {
        setState(this.state ^ state);
    }
    
    protected void setState(int state) {
        if ( this.state == state  ) {
            return;
        }
        this.state = state;
        updateStateValue();
    }
    
    private void updateStateValue() {
        V stateValue = stateValues.get(state);
        if ( stateValue == null ) {
            stateValue = stateValues.get(DEFAULT);
        }
        for ( Component component : components.keySet() ) {
            setValue(stateValue, component);
        }
    }
    
    private void setValue(V value, Component component) {
        setter.setValue(value, component);
        component.repaint();
    }
    
    public void install(Component component) {
        components.put(component, STUB);
        subscribe(component);
    }

    public void uninstall(Component component) {
        unsubscribe(component);
        components.remove(component);
    }
    
    protected void subscribe(Component component) {
        component.addFocusListener(stateProvider);
        component.addMouseListener(stateProvider);
        if ( component instanceof ItemSelectable) {
            ((ItemSelectable) component).addItemListener(stateProvider);
        }
    }

    protected void unsubscribe(Component component) {
        component.removeFocusListener(stateProvider);
        component.removeMouseListener(stateProvider);
        if ( component instanceof ItemSelectable) {
            ((ItemSelectable) component).removeItemListener(stateProvider);
        }
    }
    
    private class StateProvider implements FocusListener, MouseListener, ItemListener {

        @Override
        public void focusGained(FocusEvent e) {
            onState(FOCUSED);
        }

        @Override
        public void focusLost(FocusEvent e) {
            offState(FOCUSED);
        }

        @Override
        public void mouseClicked(MouseEvent e) {
        }

        @Override
        public void mousePressed(MouseEvent e) {
            onState(PRESSED);
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            offState(PRESSED);
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            onState(ENTERED);
        }

        @Override
        public void mouseExited(MouseEvent e) {
            offState(ENTERED);
        }
        
        @Override
        public void itemStateChanged(ItemEvent e) {
            boolean selected = (e.getStateChange() & ItemEvent.SELECTED) != 0;
            if ( selected ) {
                onState(SELECTED);
            } else {
                offState(SELECTED);
            }
        }
     
    }
}
