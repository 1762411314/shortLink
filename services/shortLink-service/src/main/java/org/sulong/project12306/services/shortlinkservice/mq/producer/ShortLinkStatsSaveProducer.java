package org.sulong.project12306.services.shortlinkservice.mq.producer;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

import static org.sulong.project12306.services.shortlinkservice.common.constant.RedisKeyConstant.SHORT_LINK_STATS_STREAM_TOPIC_KEY;

/**
 * 短链接监控状态保存消息队列生产者
 */
@Component
@RequiredArgsConstructor
public class ShortLinkStatsSaveProducer {
    private final StringRedisTemplate stringRedisTemplate;

    /**
     * 发送延迟消费短链接统计
     */
    public void send(Map<String, String> producerMap){
        stringRedisTemplate.opsForStream().add(SHORT_LINK_STATS_STREAM_TOPIC_KEY,producerMap);
    }
}
