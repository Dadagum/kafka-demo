package com.dadagum.kafka.commons.serializer;

import com.alibaba.fastjson.JSON;
import com.dadagum.kafka.commons.bean.Message;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

public class MessageDeserializer implements Deserializer<Message> {
    @Override
    public void configure(Map<String, ?> map, boolean b) {

    }

    @Override
    public Message deserialize(String s, byte[] bytes) {
        return JSON.parseObject(bytes, Message.class);
    }

    @Override
    public void close() {

    }
}
