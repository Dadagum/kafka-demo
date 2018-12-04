package com.dadagum.kafka.producer.core;

import com.dadagum.kafka.commons.bean.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

@Component
public class MessageProducer {

    private static final Logger logger = LoggerFactory.getLogger(MessageProducer.class);

    private static final Random random = new Random();

    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy:MM:dd:HH:mm:ss");

    // private final KafkaTemplate<String, Message> kafkaTemplate;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public MessageProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

//    @Scheduled(cron = "0/3 * * * * ?")
//    public void produceMessage() {
//        Message message = collect();
//        logger.info("producer is sending message = " + message);
//        kafkaTemplate.send("test", message);
//    }

    @Scheduled(cron = "0/10 * * * * ?")
    public void produceText() {
        kafkaTemplate.send("hongda-input", getText());
        logger.info("producer is sending");
    }

    /**
     * 模拟创建一条record
     */
    private Message collect() {
        Message message = new Message();
        message.setUuid(UUID.randomUUID().toString());
        message.setAnomaly(random.nextBoolean());
        message.setOriginal(random.nextBoolean());
        message.setValue(random.nextDouble() + random.nextInt(100));
        message.setRunAt(simpleDateFormat.format(new Date()));
        return message;
    }

    private String getText() {
        return "Kafka Kafka Streams";
    }
}
