package com.dadagum.kafka.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;


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
    public void getText() {
        logger.info("getting message");
    }

}
