package org.ns.gui.list;

import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.JComponent;
import javax.swing.JList;
import org.ns.func.Function;

/**
 * 
 * @author stupak
 */
public class ListEditorInstaller {
    
    private final static Function<JList, Integer> DEFAULT_EDITED_CELL_INDEX_PROVIDER = new Function<JList, Integer>() {

        @Override
        public Integer apply(JList list) {
            return list.getSelectedIndex();
        }
    };
    
    private final Function<JList, Integer> editedCellIndexProvider;

    public ListEditorInstaller() {
        this(DEFAULT_EDITED_CELL_INDEX_PROVIDER);
    }

    public ListEditorInstaller(Function<JList, Integer> editedCellIndexProvider) {
        this.editedCellIndexProvider = editedCellIndexProvider;
    }

    public Function<JList, Integer> getEditedCellIndexProvider() {
        return editedCellIndexProvider;
    }
    
    public void installEditor ( final JList list, final ListEditor listEditor )   {     
        // Собственно код, начинающий редактирование в списке
        final Runnable startEdit = new Runnable() {
            
            @Override
            public void run() {
                // Проверка на наличие выделенной ячейки
                final int index = editedCellIndexProvider.apply(list);
                if ( index == -1 ) {
                    return;
                }
                // Проверка на возможность редактирования выделенной ячейки
                final Object value = list.getModel().getElementAt(index);
                if ( !listEditor.isCellEditable (list, index, value) ) {
                    return;
                }
                // Создаём редактор
                final JComponent editor = listEditor.createEditor ( list, index, value );
                // Устанавливаем его размеры и слушатели для ресайза
                editor.setBounds(computeCellEditorBounds(index, value, list, listEditor));
                list.addComponentListener(new ComponentAdapter() {
                    
                    @Override
                    public void componentResized(ComponentEvent e) {
                        checkEditorBounds();
                    }
                    
                    private void checkEditorBounds() {
                        Rectangle newBounds = computeCellEditorBounds(index, value, list, listEditor);
                        if ( newBounds != null && !newBounds.equals(editor.getBounds()) ) {
                            editor.setBounds ( newBounds );
                            list.revalidate ();
                            list.repaint ();
                        }
                    }
                });
                
                // Добавляем компонент поверх списка
                list.add(editor);
                list.revalidate();
                list.repaint();
                // Забираем фокус в редактор
                if ( editor.isFocusable() ) {
                    editor.requestFocus();
                    editor.requestFocusInWindow();
                }
                // Создаём методы отмены и завершения редактирования
                final Runnable cancelEdit = new Runnable() {
                    
                    @Override
                    public void run() {
                        list.remove(editor);
                        listEditor.removeEditorActions(list);
                        list.revalidate();
                        list.repaint();
                        listEditor.editCancelled(list, index);
                    }
                };
                final Runnable finishEdit = new Runnable() {
                    
                    @Override
                    public void run() {
                        Object newValue = listEditor.getEditorValue(list, index, value);
                        boolean changed = listEditor.updateModelValue(list, index, newValue, true);
                        list.remove(editor);
                        listEditor.removeEditorActions(list);
                        list.revalidate();
                        list.repaint();
                        if ( changed ) {
                            listEditor.editFinished(list, index, value, newValue);
                        } else {
                            listEditor.editCancelled(list, index);
                        }
                    }
                };
                listEditor.setupEditorActions(list, value, cancelEdit, finishEdit);
                // Оповещаем о начале редактирования
                listEditor.editStarted(list, index);
            }
        };
        listEditor.installEditor(list, startEdit);
    }
    
    private Rectangle computeCellEditorBounds(int index, Object value, JList list, ListEditor listEditor) {
        // Метод возвращающий расположение редактора на списке
        Rectangle cellBounds = list.getCellBounds (index, index);
        if ( cellBounds != null ) {
            Rectangle editorBounds = listEditor.getEditorBounds(list, index, value, cellBounds);
            return new Rectangle(cellBounds.x + editorBounds.x, cellBounds.y + editorBounds.y, editorBounds.width, editorBounds.height);
        } else {
            return null;
        }
    }
    
}
