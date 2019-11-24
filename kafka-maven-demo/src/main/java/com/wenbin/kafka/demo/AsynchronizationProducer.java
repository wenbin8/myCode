package com.wenbin.kafka.demo;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import sun.dc.pr.PRError;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class AsynchronizationProducer extends Thread {
    private final KafkaProducer<Integer, String> producer;
    private final String topic;

    public AsynchronizationProducer(String topic) {
        Properties properties = new Properties();

        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.1.51:9092,192.168.1.52:9092,192.168.1.53:9092");
        properties.put(ProducerConfig.CLIENT_ID_CONFIG, "practice-producer");
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        // 默认大小是16384byte,也就是16kb， 意味着当一批消息大小达到指定的batch.size的时候会统一发送
        properties.put(ProducerConfig.BATCH_SIZE_CONFIG, "1006384");
        properties.put(ProducerConfig.LINGER_MS_CONFIG, "3000");
        properties.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, "3000");
        producer = new KafkaProducer<Integer, String>(properties);

        this.topic = topic;
    }

    @Override
    public void run() {
        int num = 0;
        while (num < 50) {
            String msg = "pratice test message:" + num;

            producer.send(new ProducerRecord<Integer, String>(topic, msg), new Callback() {
                @Override
                public void onCompletion(RecordMetadata metadata, Exception exception) {
                    System.out.println("callback:" + metadata.offset()+ "->" + metadata.partition());
                }
            });
            num++;
        }
    }

    public static void main(String[] args) throws IOException {
        new AsynchronizationProducer("test").start();
        System.in.read();
    }
}
