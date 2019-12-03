package com.wenbin.helloserviceapi;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/refactor")
public interface RefactorHelloService {



    @RequestMapping(value = "/hello4", method = RequestMethod.GET)
    String hello(@RequestParam String name);

    @RequestMapping(value = "/hello5", method = RequestMethod.GET)
    User hello(@RequestParam String name, @RequestParam Integer age);

    @RequestMapping(value = "/hello6", method = RequestMethod.POST)
    String hello(@RequestBody User user);
}
