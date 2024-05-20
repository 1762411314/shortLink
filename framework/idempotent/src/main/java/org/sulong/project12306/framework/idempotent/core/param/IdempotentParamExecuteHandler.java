package org.sulong.project12306.framework.idempotent.core.param;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.alibaba.fastjson2.JSON;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.sulong.project12306.framework.bases.constant.UserConstant;
import org.sulong.project12306.framework.convention.exception.ClientException;
import org.sulong.project12306.framework.idempotent.core.AbstractIdempotentExecuteHandler;
import org.sulong.project12306.framework.idempotent.core.IdempotentContext;
import org.sulong.project12306.framework.idempotent.core.IdempotentParamWrapper;
import org.sulong.project12306.framework.user.core.UserContext;

@RequiredArgsConstructor
public class IdempotentParamExecuteHandler extends AbstractIdempotentExecuteHandler implements IdempotentParamService {

    private final RedissonClient redissonClient;
    private static final String LOCK= "lock:param:restAPI";
    @Override
    protected IdempotentParamWrapper buildWrapper(ProceedingJoinPoint joinPoint) {
        String lockKey=String.format("idempotent:path:%s:currentUserId:%s:md5:%s",getServletPath(),getCurrentUserId(),calcArgsMD5(joinPoint));

        return IdempotentParamWrapper.builder().lockKey(lockKey).joinPoint(joinPoint).build();
    }

    private String getServletPath() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        return servletRequestAttributes.getRequest().getServletPath();
    }

    private String getCurrentUserId(){
        String userId= UserContext.getUserId();
        if (StrUtil.isBlank(userId)){
            throw new ClientException("用户ID获取失败，请登录");
        }
        return userId;
    }

    private String calcArgsMD5(ProceedingJoinPoint joinPoint){
        return DigestUtil.md5Hex(JSON.toJSONBytes(joinPoint.getArgs()));
    }
    @Override
    public void handler(IdempotentParamWrapper wrapper) {
        String lockKey= wrapper.getLockKey();
        RLock lock=redissonClient.getLock(lockKey);
        if (!lock.tryLock()){
            throw new ClientException(wrapper.getIdempotent().message());
        }
        IdempotentContext.put(LOCK,lock);
    }

    @Override
    public void postProcessing() {
        RLock lock= null;
        try{
            lock=(RLock) IdempotentContext.getKey(LOCK);
        }finally {
            if (lock != null) {
                lock.unlock();
            }
        }
    }
}
