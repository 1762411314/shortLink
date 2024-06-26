package org.sulong.project12306.framework.cache.toolkit;

import java.util.Optional;
import java.util.stream.Stream;
import com.google.common.base.Joiner;
import com.google.common.base.Strings;

public class CacheUtil {
    private static final String SPLICING_OPERATOR = "_";

    /**
     * 构建缓存标识
     *
     * @param keys
     * @return
     */
    public static String buildKey(String... keys){
        Stream.of(keys).forEach(each-> Optional.ofNullable(Strings.emptyToNull(each))
                .orElseThrow(()->new RuntimeException("构建缓存 key 不允许为空")));
        return Joiner.on(SPLICING_OPERATOR).join(keys);
    }

    public static boolean isNullOrBlank(Object cacheVal){
        return cacheVal==null||(cacheVal instanceof String && Strings.isNullOrEmpty((String) cacheVal));
    }
}
