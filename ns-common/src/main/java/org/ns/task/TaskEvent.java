package org.ns.task;

import org.ns.event.Event;

/**
 *
 * @author stupak
 */
public class TaskEvent extends Event<TaskExecutionService> {

    private final Task task;
    private final State state;
    private final Throwable error;

    public TaskEvent(TaskExecutionService source, Task task, State state) {
        this(source, task, state, null);
    }
    
    public TaskEvent(TaskExecutionService source, Task task, State state, Throwable error) {
        super(source);
        this.task = task;
        this.state = state;
        this.error = error;
    }

    public Task getTask() {
        return task;
    }

    public State getState() {
        return state;
    }

    public Throwable getError() {
        return error;
    }
    
}
