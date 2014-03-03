package org.ns.task;

/**
 *
 * @author stupak
 */
public interface TaskExecutionService {

    TaskController execute(String name, Runnable runable);
}
