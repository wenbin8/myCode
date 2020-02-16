package com.wenbin.formework.context;

import com.wenbin.formework.annotation.MyAutowired;
import com.wenbin.formework.annotation.MyController;
import com.wenbin.formework.annotation.MyService;
import com.wenbin.formework.beans.MyBeanWrapper;
import com.wenbin.formework.beans.config.MyBeanDefinition;
import com.wenbin.formework.beans.config.MyBeanPostProcessor;
import com.wenbin.formework.beans.support.MyBeanDefinitionReader;
import com.wenbin.formework.core.MyBeanFactory;
import com.wenbin.formework.beans.support.MyDefaultListableBeanFactory;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

public class MyApplicationContext extends MyDefaultListableBeanFactory implements MyBeanFactory {

    private String[] configLocations;
    private MyBeanDefinitionReader reader;

    // 单例IOC容器缓存
    private Map<String, Object> singletonObjects = new ConcurrentHashMap<String, Object>();
    // 通用IOC容器
    private Map<String, MyBeanWrapper> factoryBeanInstanceCache = new ConcurrentHashMap<String, MyBeanWrapper>();

    public MyApplicationContext(String... configLocations) {
        this.configLocations = configLocations;

        try {
            refresh();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void refresh() throws Exception {
        //1、定位，定位配置文件
        reader = new MyBeanDefinitionReader(this.configLocations);
        //2、加载配置文件，扫描相关的累，把他们封装成BeanDefinition
        List<MyBeanDefinition> beanDefinitions = reader.loadBeanDefinitions();
        //3、注册，把配置信息放到容器里面（伪IOC容器）
        doRegisterBeanDefinition(beanDefinitions);
        //4、把不是掩饰加载的累，提前初始化
        doAutowrited();
    }

    // 只处理非延时加载的情况
    private void doAutowrited() {
        for (Map.Entry<String, MyBeanDefinition> beanDefinitionEntry : super.beanDefinitionMap.entrySet()) {
            String beanName = beanDefinitionEntry.getKey();
            if (!beanDefinitionEntry.getValue().isLazyInit()) {
                try {
                    getBean(beanName);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void doRegisterBeanDefinition(List<MyBeanDefinition> beanDefinitions) throws Exception {
        for (MyBeanDefinition beanDefinition : beanDefinitions) {
            if (super.beanDefinitionMap.containsKey(beanDefinition.getFactoryBeanName())) {
                throw new Exception("The “" + beanDefinition.getFactoryBeanName() + "” is exists!!");
            }
            super.beanDefinitionMap.put(beanDefinition.getFactoryBeanName(), beanDefinition);
        }
    }

    // 依赖注入，从这里开始，通过读取BeanDefinition中的信息
    // 然后，通过反射机制创建一个实例并返回。
    // Spring做法是，不会把最原始的对象放出去，会用一个BeanWrapper来进行一次包装
    // 装饰器模式：
    //1、保留原来的OOP关系
    //2、我需要对它进行扩展，增强（为了以后AOP打基础）
    @Override
    public Object getBean(String beanName) throws Exception {
        MyBeanDefinition myBeanDefinition = this.beanDefinitionMap.get(beanName);
        Object instance = null;

        MyBeanPostProcessor postProcessor = new MyBeanPostProcessor();

        postProcessor.postProcessAfterInitialization(instance, beanName);

        instance = instantiateBean(beanName, myBeanDefinition);

        //把这个对象封装到BeanWrapper中
        MyBeanWrapper beanWrapper = new MyBeanWrapper(instance, instance.getClass());

        //拿到BeanWraoper之后，把BeanWrapper保存到IOC容器中去
        this.factoryBeanInstanceCache.put(beanName,beanWrapper);

        postProcessor.postProcessAfterInitialization(instance,beanName);

        //3、注入
        populateBean(beanName,new MyBeanDefinition(),beanWrapper);

        return this.factoryBeanInstanceCache.get(beanName).getWrappedInstance();
    }

    private void populateBean(String beanName, MyBeanDefinition myBeanDefinition, MyBeanWrapper beanWrapper) {
        Object instance = beanWrapper.getWrappedInstance();

        Class<?> clazz = beanWrapper.getWrppedClass();

        // 判断只有加了注解的累，才执行以来注入
        if (!(clazz.isAnnotationPresent(MyController.class)
                || clazz.isAnnotationPresent(MyService.class))) {
            return;
        }

        // 获得所有fields
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            if (!field.isAnnotationPresent(MyAutowired.class)) {
                continue;
            }

            MyAutowired autowired = field.getAnnotation(MyAutowired.class);

            String autowiredBeanName = autowired.value().trim();
            if ("".equals(autowiredBeanName)) {
                autowiredBeanName = field.getType().getName();
            }

            //强制访问
            field.setAccessible(true);

            try {
                //为什么会为NULL，先留个坑
                if(this.factoryBeanInstanceCache.get(autowiredBeanName) == null){ continue; }
//                if(instance == null){
//                    continue;
//                }
                field.set(instance,this.factoryBeanInstanceCache.get(autowiredBeanName).getWrappedInstance());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        }
    }

    private Object instantiateBean(String beanName, MyBeanDefinition myBeanDefinition) {
        // 1,拿到要实例化的对象类名
        String className = myBeanDefinition.getBeanClassName();

        // 2,反射实例化，得到一个对象
        Object instance = null;

        try {
            //假设默认就是单例,细节暂且不考虑，先把主线拉通
            if (this.singletonObjects.containsKey(className)) {
                instance = this.singletonObjects.get(className);
            } else {
                Class<?> clazz = Class.forName(className);
                instance = clazz.newInstance();
                this.singletonObjects.put(className, instance);
                this.singletonObjects.put(myBeanDefinition.getFactoryBeanName(), instance);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return instance;
    }

    @Override
    public Object getBean(Class<?> beanClass) throws Exception {
        return getBean(toLowerFirstCase(beanClass.getSimpleName()));
    }

    public String[] getBeanDefinitionNames() {
        return this.beanDefinitionMap.keySet().toArray(new  String[this.beanDefinitionMap.size()]);
    }

    public int getBeanDefinitionCount(){
        return this.beanDefinitionMap.size();
    }

    public Properties getConfig(){
        return this.reader.getConfig();
    }

    private String toLowerFirstCase(String simpleName) {
        char [] chars = simpleName.toCharArray();
        //之所以加，是因为大小写字母的ASCII码相差32，
        // 而且大写字母的ASCII码要小于小写字母的ASCII码
        //在Java中，对char做算学运算，实际上就是对ASCII码做算学运算
        chars[0] += 32;
        return String.valueOf(chars);
    }
}
