package org.ns.task;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 *
 * @author stupak
 */
public class TaskExecutionServiceImpl implements TaskExecutionService {

    private final Executor executor;

    public TaskExecutionServiceImpl(Executor Executor) {
        this.executor = Executor;
    }
    
    @Override
    public void execute(String taskName, Runnable task) {
        executor.execute(task);
    }

    public static final int DEFAULT_THREAD_COUNT = 3;
    
    public static TaskExecutionService getDefault() {
        return getThreadPoolService(DEFAULT_THREAD_COUNT);
    }
    
    public static TaskExecutionService getThreadPoolService(int threadCount) {
        return new TaskExecutionServiceImpl(Executors.newFixedThreadPool(threadCount));
    }
}
