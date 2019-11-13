package com.wenbin.dubbo.app;

import com.wenbin.dubbo.demo.serivce.LoginSerivce;
import com.wenbin.dubbo.demo.serivce.UserService;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {
    public static void main(String[] args) throws Exception {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[] {"/spring/application.xml"});
        context.start();
        LoginSerivce demoService = (LoginSerivce)context.getBean("loginService"); // 获取远程服务代理
        String res = demoService.login("admin", "admin"); // 执行远程方法
        System.out.println( "调用结果：" + res ); // 显示调用结果

        UserService userService = (UserService)context.getBean("userService"); // 获取远程服务代理
        userService.register(111122);
    }
}
