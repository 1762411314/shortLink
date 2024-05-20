package org.sulong.project12306.framework.idempotent.core;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.sulong.project12306.framework.idempotent.annotation.Idempotent;

import java.lang.reflect.Method;

@Aspect
public class IdempotentAspect {

    @Around("@annotation(org.sulong.project12306.framework.idempotent.annotation.Idempotent)")
    public Object idempotentHandler(ProceedingJoinPoint joinPoint) throws Throwable{
        Idempotent idempotent=getIdempotent(joinPoint);
        IdempotentExecuteHandler idempotentExecuteHandler=IdempotentExecuteHandlerFactory.getInstance(idempotent.scene(),idempotent.type());
        Object result;
        try {
            idempotentExecuteHandler.execute(joinPoint,idempotent);
            result=joinPoint.proceed();
            idempotentExecuteHandler.postProcessing();
        } catch (RepeatConsumptionException ex) {
            /**
             * 触发幂等逻辑时可能有两种情况：
             *    * 1. 消息还在处理，但是不确定是否执行成功，那么需要返回错误，方便 RocketMQ 再次通过重试队列投递
             *    * 2. 消息处理成功了，该消息直接返回成功即可
             */
            if (!ex.getError()){
                return null;
            }
            throw ex;
        }catch (Throwable ex){
            idempotentExecuteHandler.exceptionProcessing();
            throw ex;
        }finally {
            IdempotentContext.clean();
        }
        return result;
    }

    public static Idempotent getIdempotent(ProceedingJoinPoint joinPoint) throws NoSuchMethodException {
        MethodSignature methodSignature= (MethodSignature) joinPoint.getSignature();
        Method method=joinPoint.getTarget().getClass().getDeclaredMethod(methodSignature.getName(),methodSignature.getMethod().getParameterTypes());
        return method.getDeclaredAnnotation(Idempotent.class);
    }
}
