package com.wenbin.kafka.demo;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.PartitionInfo;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class MyPartitioner implements Partitioner {
    private Random random = new Random();

    @Override
    public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
        // 获取集群中指定topic的所有分区信息
        List<PartitionInfo> partitionInfoList = cluster.partitionsForTopic(topic);
        int numOfPartition = partitionInfoList.size();
        int partitionNum = 0;

        // key没有值
        if (key == null) {
            partitionNum = random.nextInt(numOfPartition); // 随机指定分区
        } else {
            partitionNum = Math.abs(key.hashCode()) % numOfPartition;
        }

        System.out.println("key:" + key + ",value:" + value + "->send to partition:" + partitionNum);

        return partitionNum;
    }

    @Override
    public void close() {

    }

    @Override
    public void configure(Map<String, ?> configs) {

    }
}
