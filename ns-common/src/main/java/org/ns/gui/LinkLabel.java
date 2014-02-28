package org.ns.gui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.TextAttribute;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JLabel;

/**
 *
 * @author stupak
 */
public class LinkLabel extends JLabel {
    
    private final MouseAdapter mouseSupport = new MouseAdapter() {

        private Cursor restoreCursor;

        @Override
        public void mouseClicked(MouseEvent e) {
            goToLink();
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            setFont(setUnderline(getFont(), true));
            restoreCursor = getCursor();
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }

        @Override
        public void mouseExited(MouseEvent e) {
            setFont(setUnderline(getFont(), false));
            setCursor(restoreCursor);
        }

        private Font setUnderline(Font font, boolean underline) {
            Map<TextAttribute, Object> attributes = new HashMap<>();
            Integer underlineValue = underline ? TextAttribute.UNDERLINE_ON : null;
            attributes.put(TextAttribute.UNDERLINE, underlineValue);
            return font.deriveFont(attributes);
        }

    };

    private GoToLinkListener goToLinkListener;

    public LinkLabel(String text) {
        this(text, null);
    }
    
    public LinkLabel(String text, GoToLinkListener goToLinkListener) {
        super(text);
        this.goToLinkListener = goToLinkListener;
        setForeground(Color.blue);
        addMouseListener(mouseSupport);
    }

    public GoToLinkListener getGoToLinkListener() {
        return goToLinkListener;
    }

    public void setGoToLinkListener(GoToLinkListener goToLinkListener) {
        if ( this.goToLinkListener == goToLinkListener ) {
            return;
        }
        this.goToLinkListener = goToLinkListener;
    }
    
    public void goToLink() {
        if ( goToLinkListener != null ) {
            goToLinkListener.goToLink();
        }
    }
    
    public static interface GoToLinkListener {
        void goToLink();
    }
}
