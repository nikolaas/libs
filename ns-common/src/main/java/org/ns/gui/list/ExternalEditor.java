package org.ns.gui.list;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.AbstractAction;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
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
        void edit(Object value);
        int getResult();
        Object getEditedValue();
    }
    
    private class ListHandler extends MouseAdapter implements Function<JList, Integer> {

        private final Runnable startEdit;
        private int currentCellIndex = -1;

        public ListHandler(Runnable startEdit) {
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

    private ListHandler listHandler;
    private JComponent editorPane;
    private JLabel valueLabel;
    private JButton showExternalSettingsButton;
    
    private final ExternalUI externalUI;
    
    private JList list;
    private Runnable cancelEdit;
    private Runnable finishEdit;
    private Object editedValue;
    private int editedIndex;
    private Function<Object, String> toStringFunction;
    private final ListSelectionListener listSelectionListener = new ListSelectionListener() {

        @Override
        public void valueChanged(ListSelectionEvent e) {
            setEditorBackground(list, editedIndex);
        }
    };

    public ExternalEditor(ExternalUI externalUI) {
        this.externalUI = externalUI;
    }
    
    private Function<JList, Integer> getEditedCellIndexProvider() {
        return listHandler;
    }
    
    public Function<Object, String> getToStringFunction() {
        return toStringFunction;
    }

    public void setToStringFunction(Function<Object, String> toStringFunction) {
        this.toStringFunction = toStringFunction;
    }
    
    @Override
    public void installEditor(JList list, final Runnable startEdit) {
        listHandler = new ListHandler(startEdit);
        list.addMouseListener(listHandler);
        list.addMouseMotionListener(listHandler);
    }

    @Override
    public JComponent createEditor(JList list, int index, Object value) {
        if ( editorPane == null ) {
            Rectangle rectangle = list.getCellBounds(index, index);
            editorPane = new JPanel();
            valueLabel = new JLabel();
            showExternalSettingsButton = createShowExternalSettingsButton();
            GroupLayout layout = new GroupLayout(editorPane);
            editorPane.setLayout(layout);
            layout.setHorizontalGroup(
                layout.createSequentialGroup()
                    .addComponent(valueLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
                    .addComponent(showExternalSettingsButton, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
            );
            layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(valueLabel, rectangle.height, rectangle.height, rectangle.height)
                    .addComponent(showExternalSettingsButton, rectangle.height, rectangle.height, rectangle.height)
            );
        }
        valueLabel.setText(editedValueToString(value));
        setEditorBackground(list, index);
        return editorPane;
    }

    private void setEditorBackground(JList list, int index) {
        Color background;
        if ( isSelected(list, index) ) {
            background = list.getSelectionBackground();
        } else {
            background = list.getBackground();
        }
        editorPane.setBackground(background);
    }

    boolean isSelected(JList list, int index) {
        for (int selected : list.getSelectedIndices() ) {
            if ( selected == index ) {
                return true;
            }
        }
        return false;
    }
    
    private JButton createShowExternalSettingsButton() {
        final JButton button = new JButton(new AbstractAction("...") {

            @Override
            public void actionPerformed(ActionEvent e) {
                 externalUI.edit(editedValue);
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

    private String editedValueToString(Object editedValue) {
        return getToStringFunction() != null ? getToStringFunction().apply(editedValue) : editedValue != null ? editedValue.toString() : null;
    }
    
    @Override
    public void setupEditorActions(JList list, Object value, Runnable cancelEdit, Runnable finishEdit) {
        this.list = list;
        this.editedValue = value;
        this.editedIndex = indexOf(list, value);
        this.cancelEdit = cancelEdit;
        this.finishEdit = finishEdit;
        list.addListSelectionListener(listSelectionListener);
    }

    @Override
    public void removeEditorActions(JList list) {
        list.removeListSelectionListener(listSelectionListener);
        listHandler.currentCellIndex = -1;
    }
    
    private int indexOf(JList list, Object item) {
        ListModel model = list.getModel();
        for ( int i = 0; i < model.getSize(); i++ ) {
            if ( model.getElementAt(i) == item ) {
                return i;
            }
        }
        return -1;
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
