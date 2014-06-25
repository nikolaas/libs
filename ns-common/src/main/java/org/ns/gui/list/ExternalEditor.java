package org.ns.gui.list;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.AbstractAction;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.ListModel;
import org.ns.func.Function;

/**
 * Редактор для редактирования элементов списка во внешнем диалоге (либо любом другом UI). <br/>
 * Редактор представляет из себя кнопку справа от элемента списка, нажатие на 
 * которую приводит к открытию диалога, в котором можно редактировать элемент списка.
 * Диалог для редактирования элемента списка предоставляет пользователь путем реализации
 * интерфейса ExternalUI. <br/>
 * Ожидание редактирования элемента в предоставленном пользователем диалоге не 
 * предусмотрено данным редактором, поэтому должно быть реализовано пользователем 
 * самостоятельно (в простейшем случае это модальный диалог).
 * @author stupak
 */
public class ExternalEditor extends AbstractListEditor {

    public static ListEditorInstaller createInstaller(ExternalEditor externalEditor) {
        return new ListEditorInstaller(externalEditor.getEditedCellIndexProvider());
    }
    
    public static final int CANCEL = 0;
    public static final int OK = 1;
    
    public static interface ExternalUI {
        /**
         * Запуск редактирования во внешнем UI
         * @param value 
         */
        void edit(Object value);
        
        /**
         * Код завершения редактирования
         * @return одно из значений {@link #CANCEL} или {@link #OK}
         */
        int getResult();
        
        /**
         * Отредактированное значение
         * @return 
         */
        Object getEditedValue();
    }
    
    private class ListHandler extends MouseAdapter implements Function<JList, Integer> {

        private Runnable startEdit;
        private int currentCellIndex = -1;

        public void setStartEdit(Runnable startEdit) {
            this.startEdit = startEdit;
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            tryChangeEditingElement(e);
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            tryChangeEditingElement(e);
        }

        void tryChangeEditingElement(MouseEvent e) {
            JList list = (JList) e.getSource();
            int indexAtCursor = locationToIndex(list, e.getPoint());
            if ( indexAtCursor == currentCellIndex ) { 
                return;
            }
            if ( currentCellIndex != -1 ) {
                cancelEdit.run();
            }
            currentCellIndex = indexAtCursor;
            if ( currentCellIndex != -1 ) {
                startEdit.run();
            }
        }
        
        private int locationToIndex(JList list, Point location) {
            ListModel model = list.getModel();
            int totalHeight = 0;
            for ( int i = 0; i < model.getSize(); i++ ) {
                int cellHeight = list.getCellBounds(i, i).height;
                totalHeight += cellHeight;
                if ( location.y < totalHeight ) {
                    return i;
                }
            }
            return -1;
        }
        
        @Override
        public void mouseExited(MouseEvent e) {
            JList list = (JList) e.getSource();
            if ( !list.contains(e.getPoint()) ) {
                exit();
            }
        }
        
        private void exit() {
            currentCellIndex = -1;
            cancelEdit.run();
        }

        @Override
        public Integer apply(JList key) {
            return currentCellIndex;
        }
        
    }

    private final ListHandler listHandler = new ListHandler();
    private JComponent editorComponent;
    
    private final ExternalUI externalUI;
    
    private Runnable cancelEdit;
    private Runnable finishEdit;
    
    public ExternalEditor(ExternalUI externalUI) {
        this.externalUI = externalUI;
    }
    
    private Function<JList, Integer> getEditedCellIndexProvider() {
        return listHandler;
    }
    
    @Override
    public void installEditor(JList list, final Runnable startEdit) {
        listHandler.setStartEdit(startEdit);
        list.addMouseListener(listHandler);
        list.addMouseMotionListener(listHandler);
    }

    @Override
    public JComponent createEditor(JList list, int index, Object value) {
        if ( editorComponent == null ) {
            Rectangle rectangle = list.getCellBounds(index, index);
            editorComponent = new JComponent() {};
            JButton showExternalUIButton = createShowExternalUIButton(value);
            GroupLayout layout = new GroupLayout(editorComponent);
            editorComponent.setLayout(layout);
            layout.setHorizontalGroup(
                layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(showExternalUIButton, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
            );
            layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(showExternalUIButton, rectangle.height, rectangle.height, rectangle.height)
            );
        }
        return editorComponent;
    }

    private JButton createShowExternalUIButton(final Object value) {
        final JButton button = new JButton(new AbstractAction("...") {

            @Override
            public void actionPerformed(ActionEvent e) {
                 externalUI.edit(value);
                 int result = externalUI.getResult();
                if ( result == OK ) {
                    finishEdit.run();
                } else if ( result == CANCEL ) {
                    cancelEdit.run();
                } else {
                    throw new RuntimeException("Unknown result: " + result + ". Result must be OK (value is 1) or CANCEL (value is 0)");
                }
            }
        
        });
        
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBorderPainted(true);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBorderPainted(false);
            }
            
        });
        
        return button;
    }

    @Override
    public void setupEditorActions(JList list, Object value, Runnable cancelEdit, Runnable finishEdit) {
        this.cancelEdit = cancelEdit;
        this.finishEdit = finishEdit;
    }

    @Override
    public void removeEditorActions(JList list) {
        listHandler.currentCellIndex = -1;
    }
    
    @Override
    public Object getEditorValue(JList list, int index, Object oldValue) {
        int result = externalUI.getResult();
        if ( result == OK ) {
            return externalUI.getEditedValue();
        } else if ( result == CANCEL ) {
            return oldValue;
        } else {
            throw new RuntimeException("Unknown result: " + result + ". Result must be OK (value is 1) or CANCEL (value is 0)");
        }
    }
    
}
