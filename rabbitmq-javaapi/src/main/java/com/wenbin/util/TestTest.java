package com.wenbin.util;

import java.util.HashSet;
import java.util.Set;

public class TestTest {
    public static void main(String[] args) {
        Set<Integer> set1 = new HashSet<Integer>();

        for(int i=0; i<100; i++){
            set1.add(i);
            set1.remove(i-1);
        }
        System.out.println(set1.size());
    }

}
