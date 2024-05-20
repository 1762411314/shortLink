package org.sulong.project12306.framework.idempotent.core.spel;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RedissonClient;
import org.sulong.project12306.framework.cache.DistributedCache;
import org.sulong.project12306.framework.idempotent.annotation.Idempotent;
import org.sulong.project12306.framework.idempotent.core.AbstractIdempotentExecuteHandler;
import org.sulong.project12306.framework.idempotent.core.IdempotentAspect;
import org.sulong.project12306.framework.idempotent.core.IdempotentParamWrapper;
import org.sulong.project12306.framework.idempotent.toolkit.SpELUtil;

@RequiredArgsConstructor
public class IdempotentSpELByMQExecuteHandler extends AbstractIdempotentExecuteHandler implements IdempotentSpELService {

    private final DistributedCache distributedCache;

    private final static int TIMEOUT = 600;
    private final static String WRAPPER = "wrapper:spEL:MQ";

    @SneakyThrows
    @Override
    protected IdempotentParamWrapper buildWrapper(ProceedingJoinPoint joinPoint) {
        Idempotent idempotent= IdempotentAspect.getIdempotent(joinPoint);
        String lockKey= (String) SpELUtil.parseKey(idempotent.key(), ((MethodSignature)joinPoint.getSignature()).getMethod(), joinPoint.getArgs());
        return IdempotentParamWrapper.builder().joinPoint(joinPoint).lockKey(lockKey).build();
    }

    @Override
    public void handler(IdempotentParamWrapper wrapper) {
        System.out.println("幂等mq没写");
    }
}
