package com.wenbin.dubbo.app;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

public class App {

    public static void main(String[] args) throws IOException {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[] {"/spring/application.xml"});
        context.start();
        System.in.read(); // 按任意键退出
    }

}
