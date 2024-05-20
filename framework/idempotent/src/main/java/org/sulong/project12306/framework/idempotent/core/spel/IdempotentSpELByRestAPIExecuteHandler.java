package org.sulong.project12306.framework.idempotent.core.spel;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.sulong.project12306.framework.convention.exception.ClientException;
import org.sulong.project12306.framework.idempotent.annotation.Idempotent;
import org.sulong.project12306.framework.idempotent.core.AbstractIdempotentExecuteHandler;
import org.sulong.project12306.framework.idempotent.core.IdempotentAspect;
import org.sulong.project12306.framework.idempotent.core.IdempotentContext;
import org.sulong.project12306.framework.idempotent.core.IdempotentParamWrapper;
import org.sulong.project12306.framework.idempotent.toolkit.SpELUtil;

@RequiredArgsConstructor
public class IdempotentSpELByRestAPIExecuteHandler extends AbstractIdempotentExecuteHandler implements IdempotentSpELService {

    private final RedissonClient redissonClient;

    private final static String LOCK = "lock:spEL:restAPI";
    @SneakyThrows
    @Override
    protected IdempotentParamWrapper buildWrapper(ProceedingJoinPoint joinPoint) {
        Idempotent idempotent= IdempotentAspect.getIdempotent(joinPoint);
        String lockKey= (String) SpELUtil.parseKey(idempotent.key(), ((MethodSignature) joinPoint.getSignature()).getMethod(), joinPoint.getArgs());
        return IdempotentParamWrapper.builder().joinPoint(joinPoint).lockKey(lockKey).build();
    }

    @Override
    public void handler(IdempotentParamWrapper wrapper) {
        String uniqueKey=wrapper.getIdempotent().uniqueKeyPrefix()+wrapper.getLockKey();
        RLock lock=redissonClient.getLock(uniqueKey);
        if (!lock.tryLock()){
            throw new ClientException(wrapper.getIdempotent().message());
        }
        IdempotentContext.put(LOCK,lock);
    }

    @Override
    public void exceptionProcessing() {
        RLock lock = null;
        try {
            lock = (RLock) IdempotentContext.getKey(LOCK);
        } finally {
            if (lock != null) {
                lock.unlock();
            }
        }
    }

    @Override
    public void postProcessing() {
        RLock lock=null;
        try {
            lock=(RLock) IdempotentContext.getKey(LOCK);
        } finally {
            if (lock != null) {
                lock.unlock();
            }
        }
    }
}
