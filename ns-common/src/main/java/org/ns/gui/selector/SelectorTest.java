package org.ns.gui.selector;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.HeadlessException;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 *
 * @author stupak
 */
public class SelectorTest extends JFrame {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                SelectorTest frame = new SelectorTest();
                frame.setVisible(true);
            }
        });
    }

    public SelectorTest() throws HeadlessException {
        super("SelectorTest");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(400, 300));
        initComponents();
        pack();
        setLocationRelativeTo(null);
    }
    
    private final Selector backgroundSelector = new Selector<>(UIManager.getColor("Button.background"), ValueSetters.BACKGROUND_SETTER)
            .configure(Selector.ENTERED | Selector.FOCUSED | Selector.PRESSED, Color.blue)
            .configure(Selector.ENTERED | Selector.FOCUSED, Color.red)
            .configure(Selector.ENTERED, Color.red);
    
    private void initComponents() {
        JButton b1= new JButton("Button 1");
        JButton b2= new JButton("Button 2");
        
        backgroundSelector.install(b1);
        
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        getContentPane().add(b1);
        getContentPane().add(b2);
    }
    
}
