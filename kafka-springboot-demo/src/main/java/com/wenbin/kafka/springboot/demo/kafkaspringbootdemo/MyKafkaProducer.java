package com.wenbin.kafka.springboot.demo.kafkaspringbootdemo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class MyKafkaProducer {
    @Autowired
    private KafkaTemplate<Integer, String> kafkaTemplate;


    public void send() {
        kafkaTemplate.send("test", 1, "msgData");
        System.out.println("消息发送完成");
    }
}
