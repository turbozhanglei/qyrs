package com.gy.resource.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author: gaolanyu
 * @date: 2020-02-14
 * @remark:
 */
public class A {
    public static void main(String[] args) {
        List<String> list=new ArrayList();
        Set<String> set=new HashSet<String>();
        list.add("a");
        list.add("a");
        list.add("b");
        for(String str:list){
            set.add(str);
        }
        System.out.println("----------"+set.toString());
    }
}
