package org.ns.task;

/**
 *
 * @author stupak
 */
public interface TaskExecutionService {

    void execute(String taskName, Runnable task);
}
