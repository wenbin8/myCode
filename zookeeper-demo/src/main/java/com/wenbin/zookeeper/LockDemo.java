package com.wenbin.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;

public class LockDemo {
    private static String CONNECTION_STR = "192.168.1.4:2181,192.168.1.4:2182,192.168.1.4:2183";


    public static void main(String[] args) {
        CuratorFramework curatorFramework = CuratorFrameworkFactory.builder()
                .connectString(CONNECTION_STR)
                .sessionTimeoutMs(5000)
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .build();

        curatorFramework.start();

        final InterProcessMutex lock = new InterProcessMutex(curatorFramework, "/locks");

        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                System.out.println(Thread.currentThread().getName() + "->尝试竞争锁");


                try {
                    lock.acquire(); // 阻塞竞争锁

                    System.out.println(Thread.currentThread().getName() + "->成功获得锁");
                    Thread.sleep(4000);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        lock.release(); //释放锁
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }, "Thread-" + i).start();
        }
    }
}
