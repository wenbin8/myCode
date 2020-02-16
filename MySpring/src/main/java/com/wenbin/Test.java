package com.wenbin;

import com.wenbin.demo.action.MyAction;
import com.wenbin.formework.context.MyApplicationContext;

public class Test {
    public static void main(String[] args) {
        MyApplicationContext myApplicationContext
                = new MyApplicationContext("classpath:application.properties");

        try {
            Object object = myApplicationContext.getBean(MyAction.class);
            System.out.println(object);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
