package com.wenbin.formework.aop.config;

import lombok.Data;

@Data
public class MyAopConfig {
    private String pointCut;
    private String aspectBefore;
    private String aspectAfter;
    private String aspectClass;
    private String aspectAfterThrow;
    private String aspectAfterThrowingName;
    
}
