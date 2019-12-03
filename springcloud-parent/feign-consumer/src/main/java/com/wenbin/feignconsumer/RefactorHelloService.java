package com.wenbin.feignconsumer;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "HELLO-PROVIDER")
public interface RefactorHelloService extends com.wenbin.helloserviceapi.RefactorHelloService {
}
