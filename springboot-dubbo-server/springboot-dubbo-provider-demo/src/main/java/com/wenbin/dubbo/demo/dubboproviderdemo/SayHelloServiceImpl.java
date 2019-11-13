package com.wenbin.dubbo.demo.dubboproviderdemo;

import com.wenbin.demo.dubbo.api.SayHelloService;
import org.apache.dubbo.config.annotation.Service;

@Service(loadbalance = "random",timeout = 50000,cluster = "failsafe")
public class SayHelloServiceImpl implements SayHelloService {
    @Override
    public String hello(String name) {
        return "hello:" + name;
    }
}
