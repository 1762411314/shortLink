package org.sulong.project12306.services.userservice.serialize;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import cn.hutool.core.util.DesensitizedUtil;
import java.io.IOException;

public class IdCardDesensitizationSerializer extends JsonSerializer<String> {

    @Override
    public void serialize(String s, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        String idCardDesensitization=DesensitizedUtil.idCardNum(s,4,4);
        jsonGenerator.writeString(idCardDesensitization);
    }
}
