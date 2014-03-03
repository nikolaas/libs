package org.ns.task;

import java.awt.EventQueue;
import java.util.concurrent.Executor;

/**
 *
 * @author stupak
 */
public class TaskUtils {

    private TaskUtils() {
    }
    
    public static Executor eventQueueExecutor() {
        return EventQueueExecutor.getInstance();
    }
    
    private static class EventQueueExecutor implements Executor {

        private static class Instancer {
            private static final EventQueueExecutor INSTANCE = new EventQueueExecutor();
        }

        public static EventQueueExecutor getInstance() {
            return Instancer.INSTANCE;
        }

        private EventQueueExecutor() {
        }
        
        @Override
        public void execute(Runnable command) {
            if ( EventQueue.isDispatchThread() ) {
                command.run();
            } else {
                EventQueue.invokeLater(command);
            }
        }
        
    }
    
}
