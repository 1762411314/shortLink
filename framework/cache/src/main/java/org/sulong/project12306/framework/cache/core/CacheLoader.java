package org.sulong.project12306.framework.cache.core;

@FunctionalInterface
public interface CacheLoader<T> {
    T load();
}
