package org.ns.gui.list;
import javax.swing.*; 
import java.awt.event.*; 

/**
 *
 * @author stupak
 */
public class StringListEditor extends AbstractListEditor {
    
    protected JTextField editor; 
    
    @Override
    public JComponent createEditor ( JList list, int index, Object value ) { 
        editor = new JTextField();
        editor.setText ( ( String ) value ); 
        editor.selectAll (); 
        return editor; 
    } 
    
    @Override
    public void setupEditorActions ( final JList list, Object value, final Runnable cancelEdit, final Runnable finishEdit ) { 
        // Устанавливаем действия завершения/отмены 
        final FocusAdapter focusAdapter = new FocusAdapter() { 
            
            @Override
            public void focusLost ( FocusEvent e ) { 
                finishEdit.run (); 
            } 
            
        }; 
        editor.addFocusListener ( focusAdapter ); 
        editor.addKeyListener ( new KeyAdapter() { 
            
            @Override
            public void keyReleased ( KeyEvent e ) { 
                if ( e.getKeyCode () == KeyEvent.VK_ENTER ) { 
                    // Для предотвращения сохранения 
                    editor.removeFocusListener ( focusAdapter ); 
                    finishEdit.run (); 
                    list.requestFocus (); 
                    list.requestFocusInWindow (); 
                } else if ( e.getKeyCode () == KeyEvent.VK_ESCAPE ) {
                    // Для предотвращения повторного сохранения 
                    editor.removeFocusListener ( focusAdapter ); 
                    cancelEdit.run (); 
                    list.requestFocus (); 
                    list.requestFocusInWindow (); 
                } 
            } 
            
        }); 
    } 
    
    @Override
    public Object getEditorValue ( JList list, int index, Object oldValue ) { 
        return editor.getText (); 
    } 
} 
