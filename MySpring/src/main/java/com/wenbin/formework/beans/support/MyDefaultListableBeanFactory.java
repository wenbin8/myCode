package com.wenbin.formework.beans.support;

import com.wenbin.formework.beans.config.MyBeanDefinition;
import com.wenbin.formework.context.support.MyAbstractApplicationContext;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MyDefaultListableBeanFactory extends MyAbstractApplicationContext {

    // 存储注册信息的BeanDefinition
    protected final Map<String, MyBeanDefinition> beanDefinitionMap = new ConcurrentHashMap<String, MyBeanDefinition>();

}
