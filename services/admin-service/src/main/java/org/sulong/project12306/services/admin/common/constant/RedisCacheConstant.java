package org.sulong.project12306.services.admin.common.constant;

/**
 * 短链接后管 Redis 缓存常量类
 */
public class RedisCacheConstant {

    /**
     * 分组创建分布式锁
     */
    public static final String LOCK_GROUP_CREATE_KEY = "short-link:lock_group-create:%s";

}
