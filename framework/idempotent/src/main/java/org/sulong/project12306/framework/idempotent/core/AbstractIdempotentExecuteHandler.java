package org.sulong.project12306.framework.idempotent.core;

import org.aspectj.lang.ProceedingJoinPoint;
import org.sulong.project12306.framework.idempotent.annotation.Idempotent;

public abstract class AbstractIdempotentExecuteHandler implements IdempotentExecuteHandler{
    /**
     * 构建幂等验证过程中所需要的参数包装器
     *
     * @param joinPoint AOP 方法处理
     * @return 幂等参数包装器
     */
    protected abstract IdempotentParamWrapper buildWrapper(ProceedingJoinPoint joinPoint);

    @Override
    public void execute(ProceedingJoinPoint joinPoint, Idempotent idempotent) {
        IdempotentParamWrapper idempotentParamWrapper=buildWrapper(joinPoint).setIdempotent(idempotent);
        handler(idempotentParamWrapper);
    }

}
