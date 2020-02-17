package com.wenbin.formework.aop.intercept;

public interface MyMethodInterceptor {
    Object invoke(MyMethodInvocation invocation) throws Throwable;
}
