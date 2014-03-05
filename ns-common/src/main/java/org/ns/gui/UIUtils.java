package org.ns.gui;

import java.awt.Component;
import java.awt.Font;
import java.awt.font.TextAttribute;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author stupak
 */
public class UIUtils {

    private UIUtils() {
    }

    public static void underline(Component component, boolean underline) {
        Map<TextAttribute, Object> attributes = new HashMap<>();
        Integer underlineValue = underline ? TextAttribute.UNDERLINE_ON : null;
        attributes.put(TextAttribute.UNDERLINE, underlineValue);
        component.setFont(component.getFont().deriveFont(attributes));
    }
    
    public static void bold(Component component, boolean bold) {
        int style = bold ? Font.BOLD : Font.PLAIN;
        component.setFont(component.getFont().deriveFont(style));
    }
    
    public static void italic(Component component, boolean italic) {
        int style = italic ? Font.ITALIC : Font.PLAIN;
        component.setFont(component.getFont().deriveFont(style));
    }
    
    public static void boldItalic(Component component, boolean boldItalic) {
        int style = boldItalic ? Font.BOLD | Font.ITALIC : Font.PLAIN;
        component.setFont(component.getFont().deriveFont(style));
    }
    
    public static void fontSize(Component component, float size) {
        component.setFont(component.getFont().deriveFont(size));
    }
}
