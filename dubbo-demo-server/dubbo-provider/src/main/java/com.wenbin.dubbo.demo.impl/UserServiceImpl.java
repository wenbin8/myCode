package com.wenbin.dubbo.demo.impl;

import com.wenbin.dubbo.demo.serivce.UserService;

public class UserServiceImpl implements UserService {


    public void register(int id) {
        System.out.println("注册Id:" + id);
    }
}
