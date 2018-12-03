package com.dadagum.kafka.commons.serializer;


import com.alibaba.fastjson.JSON;
import com.dadagum.kafka.commons.bean.Message;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

public class MessageSerializer implements Serializer<Message>{


    @Override
    public void configure(Map<String, ?> map, boolean b) {

    }

    @Override
    public byte[] serialize(String s, Message message) {
        return JSON.toJSONBytes(message);
    }

    @Override
    public void close() {

    }
}
