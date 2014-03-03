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
    public TaskController execute(String name, Runnable runable) {
        Task task = new Task(name, runable);
        executor.execute(task);
        return task;
    }

    public static final int DEFAULT_THREAD_COUNT = 3;
    
    public static TaskExecutionService getDefault() {
        return getThreadPoolExecutionService(DEFAULT_THREAD_COUNT);
    }
    
    public static TaskExecutionService getThreadPoolExecutionService(int threadCount) {
        return new TaskExecutionServiceImpl(Executors.newFixedThreadPool(threadCount));
    }
    
}
