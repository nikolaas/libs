package org.ns.task;

/**
 *
 * @author stupak
 */
public interface Task extends Runnable {
    
    String getName() throws Exception;
}
