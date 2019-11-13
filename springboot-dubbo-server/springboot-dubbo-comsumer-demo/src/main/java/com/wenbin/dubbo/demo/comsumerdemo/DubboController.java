package com.wenbin.dubbo.demo.comsumerdemo;

import com.wenbin.demo.dubbo.api.SayHelloService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DubboController {
    //Dubbo提供的注解
    @Reference(timeout = 5000,interfaceClass = SayHelloService.class)
    SayHelloService sayHelloService; //dubbo://

    @GetMapping("/sayhello")
    public String sayHello(){
        //我调用这个服务可能失败，如果失败了，我要怎么处理
        return sayHelloService.hello("wenbin");

    }



}
