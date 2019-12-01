package com.wenbin.springcloudprovider;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Logger;

@RestController
public class HelloController {

    private final Logger logger = Logger.getLogger(String.valueOf(getClass()));


    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public String index() {
        logger.info("call this provider");
        return "Hello World";
    }
}
