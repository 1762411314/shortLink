package org.sulong.project12306.framework.common.threadpool.proxy;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.tomcat.util.threads.ThreadPoolExecutor.RejectedExecutionHandler;

import java.lang.reflect.Proxy;
import java.util.concurrent.atomic.AtomicLong;
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RejectProxyUtil {
    public static RejectedExecutionHandler createProxy(RejectedExecutionHandler rejectedExecutionHandler, AtomicLong rejectedNum){
        return (RejectedExecutionHandler) Proxy
                .newProxyInstance(
                        rejectedExecutionHandler.getClass().getClassLoader(),
                        new Class[] {rejectedExecutionHandler.getClass()},
                        new RejectedProxyInvocationHandler(rejectedExecutionHandler, rejectedNum));
    }
}
