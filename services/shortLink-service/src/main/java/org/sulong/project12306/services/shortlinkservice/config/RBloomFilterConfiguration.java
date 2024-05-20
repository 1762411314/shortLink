package org.sulong.project12306.services.shortlinkservice.config;

import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 布隆过滤器配置
 */
@Configuration
public class RBloomFilterConfiguration {
    @Bean
    public RBloomFilter<String> shortUriCreateCachePenetrationBloomFilter(RedissonClient redissonClient){
        RBloomFilter<String> rBloomFilter=redissonClient.getBloomFilter("shortUriCreateCachePenetrationBloomFilter");
        rBloomFilter.tryInit(100000000L, 0.001);
        return rBloomFilter;
    }
}
