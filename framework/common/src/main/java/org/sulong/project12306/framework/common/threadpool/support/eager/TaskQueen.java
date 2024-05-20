package org.sulong.project12306.framework.common.threadpool.support.eager;

import lombok.NonNull;
import lombok.Setter;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;

public class TaskQueen<R extends Runnable> extends LinkedBlockingQueue<Runnable> {
    @Setter
    private EagerThreadPoolExecutor executor;

    public TaskQueen(int capacity) {
        super(capacity);
    }

    @Override
    public boolean offer(@NonNull Runnable runnable){
        int currentPoolThreadSize=executor.getPoolSize();
        if (currentPoolThreadSize>executor.getSubmittedTaskCount()){
            super.offer(runnable);
        }
        if (currentPoolThreadSize<executor.getMaximumPoolSize()){
            return false;
        }
        return super.offer(runnable);
    }

    public boolean retryOffer(Runnable o, long timeout, TimeUnit unit) throws InterruptedException {
        if (executor.isShutdown()) {
            throw new RejectedExecutionException("Executor is shutdown!");
        }
        return super.offer(o, timeout, unit);
    }
}
