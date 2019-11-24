package com.wenbin.kafka.demo;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.Properties;

public class Consumer extends Thread {

    private final KafkaConsumer<Integer, String> consumer;

    private final String topic;

    public Consumer(String topic) {
        Properties properties = new Properties();

        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.1.51:9092,192.168.1.52:9092,192.168.1.53:9092");

        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "practice-consumer");
        // 设置offset自动提交
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        // 自动提交时间间隔
        properties.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
        properties.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "30000");
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, IntegerDeserializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        // 对于当前groupID来说，消息的offset从最早的消息开始消费
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        consumer = new KafkaConsumer<Integer, String>(properties);

        this.topic = topic;

    }

    public static void main(String[] args) {
        new Consumer("test").start();

    }

    @Override
    public void run() {
        while (true) {
            // 消费指定分区的时候，不需要在订阅
            consumer.subscribe(Collections.singleton(this.topic));
            // 指定分区
//            TopicPartition topicPartition=new TopicPartition(topic,0);
//            consumer.assign(Arrays.asList(topicPartition));

            ConsumerRecords<Integer, String> records = consumer.poll(Duration.ofSeconds(1));

            records.forEach(record -> {
                System.out.println("key:" + record.key() + ",value: " + record.value() + ",offset:" + record.offset());
            });
        }
    }

}
