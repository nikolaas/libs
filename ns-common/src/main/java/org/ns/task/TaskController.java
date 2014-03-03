package org.ns.task;

import java.util.concurrent.Executor;
import org.ns.func.Callback;

/**
 *
 * @author stupak
 */
public interface TaskController {

    String getName();
    Runnable getTask();
    State getState();
    TaskController onFinish(Executor executor, Callback<Task> onFinishCallback);
    TaskController onError(Executor executor, Callback<Throwable> onErrorCallback);
}
