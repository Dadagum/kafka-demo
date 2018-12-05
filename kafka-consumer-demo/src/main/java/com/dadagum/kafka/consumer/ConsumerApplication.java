package com.dadagum.kafka.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;


@SpringBootApplication
public class ConsumerApplication {

    private static final Logger logger = LoggerFactory.getLogger(ConsumerApplication.class);


    public static void main(String[] args) {
        SpringApplication.run(ConsumerApplication.class);
    }

//    @KafkaListener(topics = "test", id = "EventGroup")
//    public void listen(Message message) {
//        logger.info("consumer is getting message = " + message);
//    }

    @KafkaListener(topics = "hongda-output", id = "WordCountGroup")
    public void getResult(@Payload Long count, @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) String key) {
        logger.info("counting result : " + key + " : " + count);
    }

}
