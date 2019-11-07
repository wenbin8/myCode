package com.wenbin.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.server.auth.DigestAuthenticationProvider;

import java.util.ArrayList;
import java.util.List;

public class AclDemo {
    private static java.lang.String CONNECTION_STR = "192.168.1.4:2181,192.168.1.4:2182,192.168.1.4:2183";

    public static void main(String[] args) throws Exception {

        // 节点赋权
//        demo1();
        // 访问授权节点
        demo2();
    }

    private static void demo2() throws Exception {
        CuratorFramework curatorFramework = CuratorFrameworkFactory.builder()
                .connectString(CONNECTION_STR)
                .sessionTimeoutMs(5000)
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .authorization("digest", "admin:admin".getBytes())
                .build();

        curatorFramework.start();

        List<ACL> aclList = new ArrayList<ACL>();
        ACL acl = new ACL(ZooDefs.Perms.READ | ZooDefs.Perms.WRITE,
                new Id("digest", DigestAuthenticationProvider.generateDigest("admin:admin")));

        aclList.add(acl);

        curatorFramework.create().withMode(CreateMode.PERSISTENT).withACL(aclList).forPath("/temp", "wenbin".getBytes());

        byte[] bytes = curatorFramework.getData().forPath("/temp");
        String s = new String(bytes);
        System.out.println(s);
        curatorFramework.delete().forPath("/temp");

    }


    private static void demo1() throws Exception {
        CuratorFramework curatorFramework = CuratorFrameworkFactory.builder()
                .connectString(CONNECTION_STR)
                .sessionTimeoutMs(50000)
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .build();

        curatorFramework.start();

        List<ACL> aclList = new ArrayList<ACL>();

        ACL acl = new ACL(ZooDefs.Perms.READ | ZooDefs.Perms.WRITE,
                new Id("digest", DigestAuthenticationProvider.generateDigest("admin:admin")));

        aclList.add(acl);

        curatorFramework.create().withMode(CreateMode.PERSISTENT).withACL(aclList).forPath("/auth");
    }
}
