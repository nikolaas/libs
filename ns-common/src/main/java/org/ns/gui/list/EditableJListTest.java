package org.ns.gui.list;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.util.Arrays;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
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
        final DefaultListModel model = new DefaultListModel();
        for ( String value : values ) {
            model.addElement(value);
        }
        model.addListDataListener(new ListDataListener() {

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
        });
        
        JList<String> list = new JList<>(model);
        ListEditor editor = new StringListEditor();
        ListUtils.installEditor(list, editor);
        
        JScrollPane scroll = new JScrollPane(list);
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(scroll, BorderLayout.CENTER);
    }
    
}
