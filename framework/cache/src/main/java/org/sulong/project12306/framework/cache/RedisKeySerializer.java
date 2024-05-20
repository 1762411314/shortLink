package org.sulong.project12306.framework.cache;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.nio.charset.Charset;

@RequiredArgsConstructor
public class RedisKeySerializer implements InitializingBean,RedisSerializer<String> {


    private final String charsetName;
    private final String keyPrefix;
    private Charset charset;
    @Override
    public void afterPropertiesSet() throws Exception {
        charset=Charset.forName(charsetName);
    }

    @Override
    public byte[] serialize(String value) throws SerializationException {
        return (keyPrefix+value).getBytes();

    }

    @Override
    public String deserialize(byte[] bytes) throws SerializationException {
        return new String(bytes,charset);
    }
}
