package org.ns.task;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicReference;
import org.ns.func.Callback;

/**
 *
 * @author stupak
 */
public class Task implements Runnable, TaskController {
    private final String name;
    private final Runnable task;
    private final List<ExecutionCallbackWrapper<Task>> onFinishCallbacks;
    private final List<ExecutionCallbackWrapper<Throwable>> onErrorCallbacks;
    private AtomicReference<State> state;
    private Throwable error;

    Task(String name, Runnable task) {
        this.name = name;
        this.task = task;
        this.onFinishCallbacks = new CopyOnWriteArrayList<>();
        this.onErrorCallbacks = new CopyOnWriteArrayList<>();
        this.state = new AtomicReference<>(State.INITED);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Runnable getTask() {
        return task;
    }

    @Override
    public void run() {
        try {
            if (state.compareAndSet(State.INITED, State.EXECUTING)) {
                task.run();
            } else {
                throw new IllegalStateException("Can't start executing task " + name + " because already " + state.get().toString().toLowerCase());
            }
        } catch (Exception ex) {
            error = ex;
        }
        if ( error == null ) {
            finish();
        } else {
            error();
        }
    }

    @Override
    public State getState() {
        return state.get();
    }

    private void finish() {
        if (state.compareAndSet(State.EXECUTING, State.EXECUTED)) {
            for (Callback<Task> callback : onFinishCallbacks) {
                callback.call(this);
            }
            onFinishCallbacks.clear();
        }
    }

    @Override
    public TaskController onFinish(Executor executor, Callback<Task> onFinishCallback) {
        if (state.get() == State.EXECUTED) {
            executeCallback(executor, onFinishCallback, this);
        } else {
            onFinishCallbacks.add(new ExecutionCallbackWrapper<>(executor, onFinishCallback));
        }
        return this;
    }
    
    private void error() {
        if ( state.compareAndSet(State.EXECUTING, State.ERROR) ) {
            for (Callback<Throwable> callback : onErrorCallbacks) {
                callback.call(error);
            }
            onErrorCallbacks.clear();
        }
    }

    @Override
    public TaskController onError(Executor executor, Callback<Throwable> onErrorCallback) {
        if ( state.get() == State.ERROR ) {
            executeCallback(executor, onErrorCallback, error);
        } else {
            onErrorCallbacks.add(new ExecutionCallbackWrapper<>(executor, onErrorCallback));
        }
        return this;
    }
    
    private static class ExecutionCallbackWrapper<T> implements Callback<T> {

        private final Executor executor;
        private final Callback<T> callback;

        public ExecutionCallbackWrapper(Executor executor, Callback<T> callback) {
            this.executor = executor;
            this.callback = callback;
        }
        
        @Override
        public void call(final T arg) {
            executeCallback(executor, callback, arg);
        }
        
    }
    
    private static <T> void executeCallback(Executor executor, final Callback<T> callback, final T arg) {
        executor.execute(new Runnable() {

            @Override
            public void run() {
                callback.call(arg);
            }
        });
    }
}
