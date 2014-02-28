package org.ns.task;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import org.ns.func.Callback;

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
    public ExecutionController execute(Task task) {
        ExecutionControllerImpl controller = new ExecutionControllerImpl(task);
        executor.execute(new TaskWrapper(controller));
        return controller;
    }

    public static final int DEFAULT_THREAD_COUNT = 3;
    
    public static TaskExecutionService getDefault() {
        return getThreadPoolService(DEFAULT_THREAD_COUNT);
    }
    
    public static TaskExecutionService getThreadPoolService(int threadCount) {
        return new TaskExecutionServiceImpl(Executors.newFixedThreadPool(threadCount));
    }
    
    private static class TaskWrapper implements Task {

        private final ExecutionControllerImpl controller;

        public TaskWrapper(ExecutionControllerImpl controller) {
            this.controller = controller;
        }
        
        @Override
        public String getName() throws Exception {
            return controller.getTask().getName();
        }

        @Override
        public void run() {
            try {
                controller.getTask().run();
                controller.setExecuted(true);
            } catch (Exception ex) {
                controller.setError(ex);
            }
        }
        
    }
    
    private static class ExecutionControllerImpl implements ExecutionController {

        private final Task task;
        private final List<Callback<Task>> onFinishCallbacks;
        private final List<Callback<Throwable>> onErrorCallbacks;
        private boolean executed = false;
        private Throwable error;

        public ExecutionControllerImpl(Task task) {
            this.task = task;
            this.onFinishCallbacks = new CopyOnWriteArrayList<>();
            this.onErrorCallbacks = new CopyOnWriteArrayList<>();
        }

        void setExecuted(boolean executed) {
            //TODO разобраться с многопоточность
            this.executed = executed;
            finish();
        }

        private void finish() {
            for ( Callback<Task> callback : onFinishCallbacks ) {
                callback.call(task);
            }
        }
        
        void setError(Throwable error) {
            //TODO разобраться с многопоточность
            this.error = error;
            error();
        }
        
        private void error() {
            for ( Callback<Throwable> callback : onErrorCallbacks ) {
                callback.call(error);
            }
        }
        
        @Override
        public Task getTask() {
            return task;
        }

        @Override
        public ExecutionController onFinish(Callback<Task> onFinishCallback) {
            onFinishCallbacks.add(onFinishCallback);
            if ( executed ) {
                onFinishCallback.call(task);
            }
            return this;
        }

        @Override
        public ExecutionController onError(Callback<Throwable> onErrorCallback) {
            onErrorCallbacks.add(onErrorCallback);
            if ( error != null ) {
                onErrorCallback.call(error);
            }
            return this;
        }
        
    }
}
