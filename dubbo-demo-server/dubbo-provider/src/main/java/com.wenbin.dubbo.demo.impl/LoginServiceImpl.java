package com.wenbin.dubbo.demo.impl;

import com.wenbin.dubbo.demo.serivce.LoginSerivce;

public class LoginServiceImpl implements LoginSerivce {
    public String login(String userName, String password) {
        if (userName.equals("admin") && password.equals("admin")) {
            return "SUCCESS";
        }
        return "FAILED";
    }
}
