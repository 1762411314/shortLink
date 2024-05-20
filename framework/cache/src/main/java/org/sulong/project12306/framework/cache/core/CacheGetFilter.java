package org.sulong.project12306.framework.cache.core;

@FunctionalInterface
public interface CacheGetFilter<T> {
    boolean filter(T param);
}
