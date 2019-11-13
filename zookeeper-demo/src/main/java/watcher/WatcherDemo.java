package watcher;

import org.apache.zookeeper.*;

import java.io.IOException;

public class WatcherDemo {
    private static String CONNECTION_STR = "192.168.1.4:2181,192.168.1.4:2182,192.168.1.4:2183";


    public static void main (String[] args) throws KeeperException, InterruptedException, IOException {
        ZooKeeper zookeeper = new ZooKeeper(CONNECTION_STR, 4000, new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                System.out.println("event.type" + event.getType());
            }
        });
        //创建节点
        zookeeper.create("/watch1", "0".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        zookeeper.exists("/watch1", true); //注册监听
        Thread.sleep(1000);
        zookeeper.setData("/watch1", "1".getBytes(), -1); //修改节点的值触发监听
        System.in.read();
    }
}
