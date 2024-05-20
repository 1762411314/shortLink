package org.sulong.project12306.framework.cache.config;

import lombok.AllArgsConstructor;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.sulong.project12306.framework.cache.RedisKeySerializer;
import org.sulong.project12306.framework.cache.StringRedisTemplateProxy;

@AllArgsConstructor
@EnableConfigurationProperties({BloomFilterPenetrateProperties.class, RedisDistributedProperties.class})
public class CacheAutoConfiguration {

    private final RedisDistributedProperties redisDistributedProperties;

    @Bean
    public RedisKeySerializer redisKeySerializer(){
        return new RedisKeySerializer(redisDistributedProperties.getPrefixCharset()
                ,redisDistributedProperties.getPrefix());
    }

    @Bean
    @ConditionalOnProperty(prefix = BloomFilterPenetrateProperties.PREFIX,name = "enabled",havingValue = "true")
    public RBloomFilter<String> bloomFilterPenetrateProperties(RedissonClient redissonClient, BloomFilterPenetrateProperties bloomFilterPenetrateProperties){
        RBloomFilter<String> cachePenetrationBloomFilter=redissonClient.getBloomFilter(bloomFilterPenetrateProperties.getName());
        cachePenetrationBloomFilter.tryInit(bloomFilterPenetrateProperties.getExpectedInsertions(), bloomFilterPenetrateProperties.getFalseProbability());
        return cachePenetrationBloomFilter;
    }

    @Bean
    public StringRedisTemplateProxy stringRedisTemplateProxy(RedisKeySerializer redisKeySerializer,
                                                             StringRedisTemplate stringRedisTemplate,
                                                             RedissonClient redissonClient){
        stringRedisTemplate.setStringSerializer(redisKeySerializer);
        return new StringRedisTemplateProxy(stringRedisTemplate,redissonClient,redisDistributedProperties);
    }
}
