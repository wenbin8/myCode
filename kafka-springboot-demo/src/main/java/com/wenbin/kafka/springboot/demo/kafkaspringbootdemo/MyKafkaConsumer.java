package com.wenbin.kafka.springboot.demo.kafkaspringbootdemo;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class MyKafkaConsumer {

    @KafkaListener(topics = {"test","first_topic"})
    public void listener(ConsumerRecord record){
        Optional msg=Optional.ofNullable(record.value());
        System.out.println("消息接收");
        if(msg.isPresent()){
            System.out.println(msg.get());
        }
    }
}
