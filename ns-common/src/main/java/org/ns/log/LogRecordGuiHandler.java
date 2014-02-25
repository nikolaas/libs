package org.ns.log;

import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 *
 * @author stupak
 */
public class LogRecordGuiHandler extends Handler {

    private final MessageQueue messageQueue;
    
    private LogRecordGuiHandler(ThreadGroup aplpicationThreadGroup) {
        messageQueue = new MessageQueue(aplpicationThreadGroup);
        messageQueue.start();
    }
    
    @Override
    public void publish(LogRecord record) {
        messageQueue.addMessage(new LogMessage(record));
    }

    @Override
    public void flush() {
    }

    @Override
    public void close() throws SecurityException {
        messageQueue.interrupt();
        stopGuiLogHandling(false);
    }

    private static boolean initialized = false;
    private static LogRecordGuiHandler handerInstance = null;
    
    public static void startGuiLogHandling(ThreadGroup aplpicationThreadGroup) {
        if ( initialized ) {
            return;
        }
        handerInstance = new LogRecordGuiHandler(aplpicationThreadGroup);
        Logger.getGlobal().addHandler(handerInstance);
        initialized = true;
    }
    
    public static void stopGuiLogHandling() {
        stopGuiLogHandling(true);
    }
    
    private static void stopGuiLogHandling(boolean closeHandler) {
        if ( initialized ) {
            if ( closeHandler ) {
                handerInstance.close();
            }
            Logger.getGlobal().removeHandler(handerInstance);
            handerInstance = null;
            initialized = false;
        }
        
    }
}
