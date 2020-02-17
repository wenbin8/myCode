package com.wenbin.formework.aop.aspect;

import com.wenbin.formework.aop.intercept.MyMethodInterceptor;
import com.wenbin.formework.aop.intercept.MyMethodInvocation;

import java.lang.reflect.Method;

public class MyMethodBeforeAdviceInterceptor extends MyAbstractAspectAdvice implements MyAdvice, MyMethodInterceptor {
    private MyJoinPoint joinPoint;

    public MyMethodBeforeAdviceInterceptor(Method aspectMethod, Object aspectTarget) {
        super(aspectMethod, aspectTarget);
    }

    @Override
    public Object invoke(MyMethodInvocation mi) throws Throwable {
        // 从被织入代码中才能拿到，JoinPoint
        this.joinPoint = mi;
        // 执行切面方法before
        before(mi.getMethod(), mi.getArguments(), mi.getThis());
        // 执行下一个链
        return mi.proceed();
    }

    private void before(Method method,Object[] args,Object target) throws Throwable{
        //传送了给织入参数
        //method.invoke(target);
        super.invokeAdviceMethod(this.joinPoint,null,null);
    }
}
