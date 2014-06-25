/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ns.gui.list;

import java.awt.Rectangle;
import javax.swing.JComponent;
import javax.swing.JList;

/**
 *
 * @author stupak
 */
public interface ListEditor {
    
    public void installEditor(JList list, Runnable startEdit);   
    
    public void removeEditorActions(JList list);
    
    public boolean isCellEditable(JList list, int index, Object value);   
    
    public JComponent createEditor(JList list, int index, Object value);   
    
    public Rectangle getEditorBounds(JList list, int index, Object value, Rectangle cellBounds);   
    
    public void setupEditorActions(JList list, Object value, Runnable cancelEdit, Runnable finishEdit);   
    
    public Object getEditorValue(JList list, int index, Object oldValue);   
    
    public boolean updateModelValue(JList list, int index, Object value, boolean updateSelection);   
    
    public void editStarted(JList list, int index);   
    
    public void editFinished(JList list, int index, Object oldValue, Object newValue);   
    
    public void editCancelled(JList list, int index); 

}
