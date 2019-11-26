package com.webin.basic;


import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = "com.webin.basic")
public class BasicSender {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(BasicSender.class);
        RabbitAdmin rabbitAdmin = context.getBean(RabbitAdmin.class);
        RabbitTemplate rabbitTemplate = context.getBean(RabbitTemplate.class);

        rabbitTemplate.convertAndSend("","BASIC_FIRST_QUEUE","-------- a direct msg");

        rabbitTemplate.convertAndSend("BASIC_TOPIC_EXCHANGE","shanghai.my.teacher","-------- a topic msg : shanghai.my.teacher");
        rabbitTemplate.convertAndSend("BASIC_TOPIC_EXCHANGE","changsha.my.student","-------- a topic msg : changsha.my.student");

        rabbitTemplate.convertAndSend("BASIC_FANOUT_EXCHANGE","","-------- a fanout msg");


    }
}
