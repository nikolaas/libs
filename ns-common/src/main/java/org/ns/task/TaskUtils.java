package org.ns.task;

/**
 *
 * @author stupak
 */
public class TaskUtils {

    private TaskUtils() {
    }
    
    public static Task newTask(String name, Runnable runnable) {
        return new TaskImpl(name, runnable);
    }
}
