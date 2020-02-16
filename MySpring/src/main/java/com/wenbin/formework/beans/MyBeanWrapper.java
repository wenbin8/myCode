package com.wenbin.formework.beans;

public class MyBeanWrapper {
    private Object wrappedInstance;
    private Class<?> wrppedClass;

    public MyBeanWrapper(Object wrappedInstance, Class<?> aClass) {
        this.wrappedInstance = wrappedInstance;
        this.wrppedClass = aClass;
    }

    public Object getWrappedInstance() {
        return wrappedInstance;
    }

    public Class<?> getWrppedClass() {
        return wrppedClass;
    }
}
