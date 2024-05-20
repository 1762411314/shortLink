package org.sulong.project12306.framework.cache.core;

@FunctionalInterface
public interface CacheGetIfAbsent<T> {
    void execute(T param);
}
