package com.wenbin.feignconsumer;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "hello-provider", fallback = HelloServiceFailback.class)
public interface HelloService {

    @RequestMapping("/hello")
    String hello();


    @RequestMapping(value = "/hello1", method = RequestMethod.GET)
    String hello(@RequestParam String name);

    @RequestMapping(value = "/hello2", method = RequestMethod.GET)
    User hello(@RequestParam String name, @RequestParam Integer age);

    @RequestMapping(value = "/hello3", method = RequestMethod.POST)
    String hello(@RequestBody User user);
}
