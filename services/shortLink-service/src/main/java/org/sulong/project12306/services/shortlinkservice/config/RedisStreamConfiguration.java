package org.sulong.project12306.services.shortlinkservice.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.stream.Consumer;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;
import org.sulong.project12306.services.shortlinkservice.mq.consumer.ShortLinkStatsSaveConsumer;

import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.sulong.project12306.services.shortlinkservice.common.constant.RedisKeyConstant.SHORT_LINK_STATS_STREAM_GROUP_KEY;
import static org.sulong.project12306.services.shortlinkservice.common.constant.RedisKeyConstant.SHORT_LINK_STATS_STREAM_TOPIC_KEY;

@Configuration
@RequiredArgsConstructor
public class RedisStreamConfiguration {
    private final RedisConnectionFactory redisConnectionFactory;
    private final ShortLinkStatsSaveConsumer shortLinkStatsSaveConsumer;

    @Bean
    public ExecutorService asyncStreamConsumer(){
        AtomicInteger index=new AtomicInteger();
        int processors=Runtime.getRuntime().availableProcessors();
        return new ThreadPoolExecutor(
                processors,
                processors+processors>>1,
                60,
                TimeUnit.MINUTES,
                new LinkedBlockingQueue<>(),
                runnable->{
                    Thread thread=new Thread(runnable);
                    thread.setName("stream_consumer_short-link_stats_" + index.incrementAndGet());
                    thread.setDaemon(true);
                    return thread;
                }
        );
    }

    @Bean(initMethod = "start",destroyMethod = "stop")
    public StreamMessageListenerContainer<String, MapRecord<String,String,String>> streamMessageListenerContainer(ExecutorService asyncStreamConsumer){
        var options=StreamMessageListenerContainer.StreamMessageListenerContainerOptions.builder()
                .executor(asyncStreamConsumer)
                .batchSize(10)
                .pollTimeout(Duration.ofSeconds(3))
                .build();
        StreamMessageListenerContainer<String, MapRecord<String,String,String>> listenerContainer=
                StreamMessageListenerContainer.create(redisConnectionFactory,options);
        listenerContainer.receiveAutoAck(Consumer.from(SHORT_LINK_STATS_STREAM_GROUP_KEY,"stats_consumer"),
                StreamOffset.create(SHORT_LINK_STATS_STREAM_TOPIC_KEY, ReadOffset.lastConsumed()),shortLinkStatsSaveConsumer);
        return listenerContainer;
    }
}
