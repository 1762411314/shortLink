package org.sulong.project12306.services.userservice.toolkit;

import static org.sulong.project12306.services.userservice.common.constant.Project12306Constant.USER_REGISTER_REUSE_SHARDING_COUNT;

public final class UserReuseUtil {

    /**
     * 计算分片位置
     */
    public static int hashShardingIdx(String username){
        return Math.abs(username.hashCode() % USER_REGISTER_REUSE_SHARDING_COUNT);
    }
}
