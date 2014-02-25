package org.ns.log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import javax.swing.SwingUtilities;

/**
 *
 * @author stupak
 */
public class MessageQueue extends Thread {

    private final Lock lock = new ReentrantLock();
    private final ConcurrentLinkedDeque<LogMessage> queue;
    private final GuiLoggingDialog dialog;
    
    private final Runnable refreshDialog = new Runnable() {

        @Override
        public void run() {
            List<LogMessage> messages = getUnhandledMessges();
            dialog.update(messages);
            if ( dialog.isShowOnNewMessage() ) {
                dialog.setVisible(true);
            }
        }
    };
    
    public MessageQueue(ThreadGroup aplicationThreadGroup) {
        super(aplicationThreadGroup, "logRecordGuiHander");
        setDaemon(true);
        queue = new ConcurrentLinkedDeque<>();
        dialog = createDialog();
    }
    
    private GuiLoggingDialog createDialog() {
        return new GuiLoggingDialog(null);
    }

    public void addMessage(LogMessage message) {
        if ( !lock.tryLock() ) {
            lock.lock();
        }
        try {
            queue.offer(message);
            fireAddNewMessage();
        } finally {
            lock.unlock();
        }
    }
    
    public List<LogMessage> getUnhandledMessges() {
        List<LogMessage> messages;
        if ( !lock.tryLock() ) {
            lock.lock();
        }
        try {
            messages = new ArrayList<>(queue);
            queue.clear();
        } finally {
            lock.unlock();
        }
        return messages;
    }
    
    private void fireAddNewMessage() {
        SwingUtilities.invokeLater(refreshDialog);
    }
    
}
