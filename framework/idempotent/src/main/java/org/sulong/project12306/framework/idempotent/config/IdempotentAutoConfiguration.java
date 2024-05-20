package org.sulong.project12306.framework.idempotent.config;

import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.sulong.project12306.framework.cache.DistributedCache;
import org.sulong.project12306.framework.idempotent.core.IdempotentAspect;
import org.sulong.project12306.framework.idempotent.core.param.IdempotentParamExecuteHandler;
import org.sulong.project12306.framework.idempotent.core.spel.IdempotentSpELByMQExecuteHandler;
import org.sulong.project12306.framework.idempotent.core.spel.IdempotentSpELByRestAPIExecuteHandler;
import org.sulong.project12306.framework.idempotent.core.token.IdempotentTokenController;
import org.sulong.project12306.framework.idempotent.core.token.IdempotentTokenExecuteHandler;
import org.sulong.project12306.framework.idempotent.core.token.IdempotentTokenService;

@EnableConfigurationProperties(IdempotentProperties.class)

public class IdempotentAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public IdempotentAspect idempotentAspect(){
        return new IdempotentAspect();
    }
    @Bean
    @ConditionalOnMissingBean
    public IdempotentParamExecuteHandler idempotentParamExecuteHandler(RedissonClient redissonClient){
        return new IdempotentParamExecuteHandler(redissonClient);
    }
    @Bean
    @ConditionalOnMissingBean
    public IdempotentTokenExecuteHandler idempotentTokenExecuteHandler(IdempotentProperties idempotentProperties,
                                                                       DistributedCache distributedCache){
        return new IdempotentTokenExecuteHandler(idempotentProperties,distributedCache);
    }
    @Bean
    public IdempotentTokenController idempotentTokenController(IdempotentTokenService idempotentTokenService){
        return new IdempotentTokenController(idempotentTokenService);
    }
    @Bean
    @ConditionalOnMissingBean
    public IdempotentSpELByRestAPIExecuteHandler idempotentSpELByRestAPIExecuteHandler(RedissonClient redissonClient){
        return new IdempotentSpELByRestAPIExecuteHandler(redissonClient);
    }
    @Bean
    @ConditionalOnMissingBean
    public IdempotentSpELByMQExecuteHandler idempotentSpELByMQExecuteHandler(DistributedCache distributedCache){
        return new IdempotentSpELByMQExecuteHandler(distributedCache);
    }
}
