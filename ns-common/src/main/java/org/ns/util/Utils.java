package org.ns.util;

import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;

/**
 *
 * @author stupak
 */
public class Utils {

    private Utils() {
    }

    public static Throwable closeSilent(java.io.Closeable closeable) {
        Throwable error = null;
        if ( closeable != null ) {
            try {
                closeable.close();
            } catch (Exception ex) {
                error = ex;
            }
        }
        return error;
    }
    
    public static void invokeAndWait(Runnable runnable) {
        try {
            SwingUtilities.invokeAndWait(runnable);
        } catch (InterruptedException | InvocationTargetException ex) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void invokeWhenUIReady(Runnable runnable) {
        SwingUtilities.invokeLater(runnable);
    }
}
