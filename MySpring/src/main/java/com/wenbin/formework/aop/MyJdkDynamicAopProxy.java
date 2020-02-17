package com.wenbin.formework.aop;

import com.wenbin.formework.aop.intercept.MyMethodInvocation;
import com.wenbin.formework.aop.support.MyAdvisedSupport;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

public class MyJdkDynamicAopProxy implements MyAopProxy, InvocationHandler {
    private MyAdvisedSupport advised;

    public MyJdkDynamicAopProxy(MyAdvisedSupport config){
        this.advised = config;
    }

    @Override
    public Object getProxy() {
        return getproxy(this.advised.getTargetClass().getClassLoader());
    }

    @Override
    public Object getproxy(ClassLoader classLoader) {
        return Proxy.newProxyInstance(classLoader, this.advised.getTargetClass().getInterfaces(), this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        List<Object> interceptorsAndDynamicMatchers =
                this.advised.getInterceptorsAndDynamicInterceptionAdvice(method, this.advised.getTargetClass());

        MyMethodInvocation invocation = new MyMethodInvocation(proxy, this.advised.getTarget(), method, args
                , this.advised.getTargetClass(), interceptorsAndDynamicMatchers);

        return invocation.proceed();
    }
}
