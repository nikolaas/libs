package org.ns.task;

import org.ns.util.Assert;

/**
 *
 * @author stupak
 */
class TaskImpl implements Task {

    private final String name;
    private final Runnable runnable;

    public TaskImpl(String name, Runnable runnable) {
        Assert.isNotNull(runnable, "runnable is null");
        this.name = name;
        this.runnable = runnable;
    }

    @Override
    public String getName() throws Exception {
        return name;
    }

    @Override
    public void run() {
        runnable.run();
    }

    @Override
    public String toString() {
        return "TaskImpl{" + "name=" + name + '}';
    }
    
}
