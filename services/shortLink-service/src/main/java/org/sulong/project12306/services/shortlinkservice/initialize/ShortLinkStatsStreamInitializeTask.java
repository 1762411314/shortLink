package org.sulong.project12306.services.shortlinkservice.initialize;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import static org.sulong.project12306.services.shortlinkservice.common.constant.RedisKeyConstant.SHORT_LINK_STATS_STREAM_GROUP_KEY;
import static org.sulong.project12306.services.shortlinkservice.common.constant.RedisKeyConstant.SHORT_LINK_STATS_STREAM_TOPIC_KEY;

@Component
@RequiredArgsConstructor
public class ShortLinkStatsStreamInitializeTask implements InitializingBean {
    private final StringRedisTemplate stringRedisTemplate;

    @Override
    public void afterPropertiesSet() throws Exception {
        Boolean hasKey = stringRedisTemplate.hasKey(SHORT_LINK_STATS_STREAM_TOPIC_KEY);
        if (hasKey == null || !hasKey) {
            stringRedisTemplate.opsForStream().createGroup(SHORT_LINK_STATS_STREAM_TOPIC_KEY, SHORT_LINK_STATS_STREAM_GROUP_KEY);
        }
    }
}
