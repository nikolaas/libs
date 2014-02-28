package org.ns.task;

import org.ns.func.Callback;

/**
 *
 * @author stupak
 */
public interface ExecutionController {

    Task getTask();
    ExecutionController onFinish(Callback<Task> onFinishCallback);
    ExecutionController onError(Callback<Throwable> onErrorCallback);
}
