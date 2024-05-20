package org.sulong.project12306.services.userservice.config;

import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(UserRegisterBloomFilterProperties.class)
public class RBloomFilterConfiguration {

    /**
     * 防止用户注册缓存穿透的布隆过滤器
     */

    @Bean
    public RBloomFilter<String> userRegisterCachePenetrationBloomFilter(RedissonClient redissonClient
            , UserRegisterBloomFilterProperties userRegisterBloomFilterP){
        RBloomFilter<String> bloomFilter=redissonClient.getBloomFilter(userRegisterBloomFilterP.getName());
        bloomFilter.tryInit(userRegisterBloomFilterP.getExpectedInsertions(), userRegisterBloomFilterP.getFalseProbability());
        return bloomFilter;
    }
}
