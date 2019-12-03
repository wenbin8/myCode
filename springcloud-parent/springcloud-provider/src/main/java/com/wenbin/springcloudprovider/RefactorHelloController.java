package com.wenbin.springcloudprovider;

import com.wenbin.helloserviceapi.RefactorHelloService;
import com.wenbin.helloserviceapi.User;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RefactorHelloController implements RefactorHelloService {

    @Override
    public String hello(String name) {
        return "hello " + name;
    }

    @Override
    public User hello(String name, Integer age) {
        return new User(name, age);
    }

    @Override
    public String hello(User user) {
        return "Hello " + user.getName() + "," + user.getAge();
    }
}
