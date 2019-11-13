package com.wenbin.dubbo.demo.comsumerdemo;

import com.wenbin.demo.dubbo.api.SayHelloService;

/**
 * 降级处理的类
 */
public class SayHelloServiceMock implements SayHelloService {

    @Override
    public String hello(String name) {
        return "服务端发生异常， 被降级了。返回兜底数据。。。";
    }
}
