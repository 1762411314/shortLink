package org.sulong.project12306.framework.cache;

import com.alibaba.fastjson2.JSON;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.sulong.project12306.framework.bases.Singleton;
import org.sulong.project12306.framework.cache.config.RedisDistributedProperties;
import org.sulong.project12306.framework.cache.core.CacheGetFilter;
import org.sulong.project12306.framework.cache.core.CacheGetIfAbsent;
import org.sulong.project12306.framework.cache.core.CacheLoader;
import org.sulong.project12306.framework.cache.toolkit.CacheUtil;
import org.sulong.project12306.framework.cache.toolkit.FastJson2Util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public class StringRedisTemplateProxy implements DistributedCache{

    private final StringRedisTemplate stringRedisTemplate;
    private final RedissonClient redissonClient;
    private final RedisDistributedProperties redisDistributedProperties;
    private static final String LUA_PUT_IF_ALL_ABSENT_SCRIPT_PATH = "lua/putIfAllAbsent.lua";
    private static final String SAFE_GET_DISTRIBUTED_LOCK_KEY_PREFIX = "safe_get_distributed_lock_get:";

    @Override
    public <T> T get(String key, Class<T> clazz) {
        String value=stringRedisTemplate.opsForValue().get(key);
        if (String.class.isAssignableFrom(clazz)){
            return (T) value;
        }
        return JSON.parseObject(value, FastJson2Util.buildType(clazz));
    }

    @Override
    public void put(String key, Object value) {
        put(key,value, redisDistributedProperties.getValueTimeout());
    }

    @Override
    public Boolean putIfAllAbsent(Collection<String> keys) {
        DefaultRedisScript<Boolean> actual= Singleton.get(LUA_PUT_IF_ALL_ABSENT_SCRIPT_PATH,()->{
            DefaultRedisScript<Boolean> redisScript=new DefaultRedisScript<>();
            redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource(LUA_PUT_IF_ALL_ABSENT_SCRIPT_PATH)));
            redisScript.setResultType(Boolean.class);
            return redisScript;
        });
        Boolean result = stringRedisTemplate.execute(actual, new ArrayList<>(keys),redisDistributedProperties.getValueTimeout().toString());
        return result!=null&&result;
    }

    @Override
    public Boolean delete(String key) {
        return stringRedisTemplate.delete(key);
    }

    @Override
    public Long delete(Collection<String> keys) {
        return stringRedisTemplate.delete(keys);
    }

    @Override
    public Boolean hasKey(String key) {
        return stringRedisTemplate.hasKey(key);
    }

    @Override
    public StringRedisTemplate getInstance() {
        return stringRedisTemplate;
    }

    @Override
    public <T> T get(String key, Class<T> clazz, CacheLoader<T> cacheLoader, long timeout) {
        return get(key,clazz,cacheLoader,timeout,redisDistributedProperties.getValueTimeUnit());
    }

    @Override
    public <T> T get(String key, Class<T> clazz, CacheLoader<T> cacheLoader, long timeout, TimeUnit timeUnit) {
        T result=get(key,clazz);
        if (CacheUtil.isNullOrBlank(result)){
            return loadAndSet(key, cacheLoader, timeout, timeUnit, false, null);
        }
        return result;
    }

    @Override
    public <T> T safeGet(String key, Class<T> clazz, CacheLoader<T> cacheLoader, long timeout) {
        return safeGet(key,clazz,cacheLoader,timeout,redisDistributedProperties.getValueTimeUnit());
    }

    @Override
    public <T> T safeGet(String key, Class<T> clazz, CacheLoader<T> cacheLoader, long timeout, TimeUnit timeUnit) {
        return safeGet(key,clazz,cacheLoader,timeout,timeUnit, null);
    }

    @Override
    public <T> T safeGet(String key, Class<T> clazz, CacheLoader<T> cacheLoader, long timeout, RBloomFilter<String> bloomFilter) {
        return safeGet(key,clazz,cacheLoader,timeout,redisDistributedProperties.getValueTimeUnit(), bloomFilter);
    }

    @Override
    public <T> T safeGet(String key, Class<T> clazz, CacheLoader<T> cacheLoader, long timeout, TimeUnit timeUnit, RBloomFilter<String> bloomFilter) {
        return safeGet(key,clazz,cacheLoader,timeout,timeUnit, bloomFilter,null,null);
    }

    @Override
    public <T> T safeGet(String key, Class<T> clazz, CacheLoader<T> cacheLoader, long timeout, RBloomFilter<String> bloomFilter, CacheGetFilter<String> cacheCheckFilter) {
        return safeGet(key,clazz,cacheLoader,timeout,redisDistributedProperties.getValueTimeUnit(), bloomFilter,cacheCheckFilter,null);
    }

    @Override
    public <T> T safeGet(String key, Class<T> clazz, CacheLoader<T> cacheLoader, long timeout, TimeUnit timeUnit, RBloomFilter<String> bloomFilter, CacheGetFilter<String> cacheCheckFilter) {
        return safeGet(key,clazz,cacheLoader,timeout,timeUnit, bloomFilter,cacheCheckFilter,null);
    }

    @Override
    public <T> T safeGet(String key, Class<T> clazz, CacheLoader<T> cacheLoader, long timeout, RBloomFilter<String> bloomFilter, CacheGetFilter<String> cacheCheckFilter, CacheGetIfAbsent<String> cacheGetIfAbsent) {
        return safeGet(key,clazz,cacheLoader,timeout,redisDistributedProperties.getValueTimeUnit(), bloomFilter,cacheCheckFilter,cacheGetIfAbsent);
    }

    @Override
    public <T> T safeGet(String key, Class<T> clazz, CacheLoader<T> cacheLoader, long timeout, TimeUnit timeUnit, RBloomFilter<String> bloomFilter, CacheGetFilter<String> cacheCheckFilter, CacheGetIfAbsent<String> cacheGetIfAbsent) {
        T result=get(key,clazz);
        if (!CacheUtil.isNullOrBlank(result)
            || Optional.ofNullable(cacheCheckFilter).map(each->each.filter(key)).orElse(false)
            || Optional.ofNullable(bloomFilter).map(each->!each.contains(key)).orElse(false)){
            return result;
        }
        RLock lock=redissonClient.getLock(SAFE_GET_DISTRIBUTED_LOCK_KEY_PREFIX+key);
        lock.lock();
        try {
            if (CacheUtil.isNullOrBlank(result=get(key,clazz))){
                if (CacheUtil.isNullOrBlank(result=loadAndSet(key,cacheLoader,timeout,timeUnit,true,bloomFilter))){
                    Optional.ofNullable(cacheGetIfAbsent).ifPresent(each -> each.execute(key));
                }
            }

        }finally {
            lock.unlock();
        }
        return result;
    }

    @Override
    public void put(String key, Object value, long timeout) {
        put(key,value,timeout,redisDistributedProperties.getValueTimeUnit());
    }

    @Override
    public void put(String key, Object value, long timeout, TimeUnit timeUnit) {
        String actual = value instanceof String ? (String) value : JSON.toJSONString(value);
        stringRedisTemplate.opsForValue().set(key,actual,timeout,timeUnit);
    }

    @Override
    public void safePut(String key, Object value, long timeout, RBloomFilter<String> bloomFilter) {
        safePut(key, value, timeout, redisDistributedProperties.getValueTimeUnit(), bloomFilter);
    }

    @Override
    public void safePut(String key, Object value, long timeout, TimeUnit timeUnit, RBloomFilter<String> bloomFilter) {
        put(key, value, timeout, timeUnit);
        if (bloomFilter != null) {
            bloomFilter.add(key);
        }
    }

    @Override
    public Long countExistingKeys(@NotNull String... keys) {
        return stringRedisTemplate.countExistingKeys(List.of(keys));
    }

    private <T> T loadAndSet(String key, CacheLoader<T> cacheLoader, long timeout, TimeUnit timeUnit, boolean safeFlag, RBloomFilter<String> bloomFilter) {
        T result=cacheLoader.load();
        if (CacheUtil.isNullOrBlank(result)) {
            return result;
        }
        if (safeFlag){
            safePut(key,result,timeout,timeUnit,bloomFilter);
        }else {
            put(key,result,timeout,timeUnit);
        }
        return result;
    }
}
