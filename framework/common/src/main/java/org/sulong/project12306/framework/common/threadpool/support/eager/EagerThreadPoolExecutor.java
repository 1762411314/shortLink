package org.sulong.project12306.framework.common.threadpool.support.eager;

import org.apache.tomcat.util.threads.ThreadPoolExecutor;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class EagerThreadPoolExecutor extends ThreadPoolExecutor {
    public EagerThreadPoolExecutor(int corePoolSize,
                                   int maximumPoolSize,
                                   long keepAliveTime,
                                   TimeUnit unit,
                                   TaskQueen<Runnable> workQueue,
                                   ThreadFactory factory,
                                   RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue,factory,handler);
    }

    private final AtomicInteger submittedTaskCount=new AtomicInteger(0);

    public int getSubmittedTaskCount(){
        return submittedTaskCount.get();
    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        submittedTaskCount.decrementAndGet();
    }

    @Override
    public void execute(Runnable command) {
        submittedTaskCount.incrementAndGet();
        try {
            super.execute(command);
        }catch (RejectedExecutionException ex){
            TaskQueen taskQueen=(TaskQueen) super.getQueue();
            try {
                if (!taskQueen.retryOffer(command,0,TimeUnit.MILLISECONDS)) {
                    submittedTaskCount.decrementAndGet();
                    throw new RejectedExecutionException("Queue capacity is full.", ex);
                }
            } catch (InterruptedException e) {
                submittedTaskCount.decrementAndGet();
                throw new RejectedExecutionException(e);
            }
        }catch (Exception ex){
            submittedTaskCount.decrementAndGet();
            throw ex;
        }
    }
}
