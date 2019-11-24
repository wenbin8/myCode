package com.wenbin.kafka.springboot.demo.kafkaspringbootdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class ProducerApplication {

    public static void main(String[] args) throws InterruptedException {
        ConfigurableApplicationContext ctx
                = SpringApplication.run(ProducerApplication.class, args);

        MyKafkaProducer producer = ctx.getBean(MyKafkaProducer.class);

        for (int i = 0; i < 10; i++) {
            producer.send();

            TimeUnit.SECONDS.sleep(2);
        }
    }
}
