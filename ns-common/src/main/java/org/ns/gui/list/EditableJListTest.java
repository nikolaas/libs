package org.ns.gui.list;

import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.LayoutStyle;
import javax.swing.SwingUtilities;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import org.ns.util.Collections;

/**
 *
 * @author stupak
 */
public class EditableJListTest extends JFrame {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                EditableJListTest frame = new EditableJListTest();
                frame.setVisible(true);
            }
        });
    }

    public EditableJListTest() throws HeadlessException {
        super("EditableJListTest");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(400, 300));
        initComponents();
        pack();
        setLocationRelativeTo(null);
    }
    
    private final List<String> values = Arrays.asList(
            "One",
            "Two",
            "Three",
            "Four",
            "Five"
    );
    
    private void initComponents() {
        JList<String> list1 = createEditableStringList();
        JList<String> list2 = createExternalSettingsStringList();
        
        JScrollPane scroll1 = new JScrollPane(list1);
        JScrollPane scroll2 = new JScrollPane(list2);
        
        JLabel label1 = new JLabel("Текстовый редактор");
        JLabel label2 = new JLabel("Внешний редактор");
        
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        
        layout.setHorizontalGroup(
            layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(label1)
                    .addComponent(scroll1)
                )
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(label2)
                    .addComponent(scroll2)
                )
        );
        layout.setVerticalGroup(
            layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(label1)
                    .addComponent(label2)
                )
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(scroll1)
                    .addComponent(scroll2)
                )
        );
    }
    
    private JList createEditableStringList() {
        final DefaultListModel model = new DefaultListModel();
        for ( String value : values ) {
            model.addElement(value);
        }
        model.addListDataListener(new DebufInfoPrinter(model));
        JList<String> list = new JList<>(model);
        ListEditor editor = new StringListEditor();
        ListEditorInstaller listEditorInstaller = new ListEditorInstaller();
        listEditorInstaller.installEditor(list, editor);
        return list;
    }
    
    private JList createExternalSettingsStringList() {
        final DefaultListModel model = new DefaultListModel();
        for ( String value : values ) {
            model.addElement(value);
        }
        model.addListDataListener(new DebufInfoPrinter(model));
        JList<String> list = new JList<>(model);
        ExternalEditor editor = new ExternalEditor(new TestExternalUI());
        ListEditorInstaller listEditorInstaller = ExternalEditor.createInstaller(editor);
        listEditorInstaller.installEditor(list, editor);
        return list;
    }

    private static class DebufInfoPrinter implements ListDataListener {

        private final DefaultListModel model;

        public DebufInfoPrinter(DefaultListModel model) {
            this.model = model;
        }

        @Override
        public void intervalAdded(ListDataEvent e) {
            print(e);
        }

        @Override
        public void intervalRemoved(ListDataEvent e) {
            print(e);
        }

        @Override
        public void contentsChanged(ListDataEvent e) {
            print(e);
        }

        private void print(ListDataEvent e) {
            System.out.println("------------------------");
            System.out.println(Collections.join(model.elements(), ", "));
            System.out.println("------------------------");
        }
    }

    private static class TestExternalUI implements ExternalEditor.ExternalUI {

        int result = ExternalEditor.CANCEL;

        @Override
        public void edit(Object value) {
            final JDialog dialog = new JDialog();
            dialog.setModal(true);
            dialog.setPreferredSize(new Dimension(300, 200));
            JButton ok = new JButton(new AbstractAction("Ok") {
                
                @Override
                public void actionPerformed(ActionEvent e) {
                    result = ExternalEditor.OK;
                    dialog.setVisible(false);
                }
            });
            JButton cancel = new JButton(new AbstractAction("Cancel") {
                
                @Override
                public void actionPerformed(ActionEvent e) {
                    result = ExternalEditor.CANCEL;
                    dialog.setVisible(false);
                }
            });
            WindowAdapter windowAdapter = new WindowAdapter() {
                
                @Override
                public void windowClosing(WindowEvent e) {
                    result = ExternalEditor.CANCEL;
                }
                
            };
            dialog.addWindowListener(windowAdapter);
            dialog.getContentPane().setLayout(new BoxLayout(dialog.getContentPane(), BoxLayout.LINE_AXIS));
            dialog.getContentPane().add(ok);
            dialog.getContentPane().add(cancel);
            dialog.pack();
            dialog.setLocationRelativeTo(null);
            dialog.setVisible(true);
        }

        @Override
        public int getResult() {
            return result;
        }

        @Override
        public Object getEditedValue() {
            return "editedValue";
        }
    }
    
}
