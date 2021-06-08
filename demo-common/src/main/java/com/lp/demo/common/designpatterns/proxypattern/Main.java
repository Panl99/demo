package com.lp.demo.common.designpatterns.proxypattern;

/**
 * @create 2021/3/1 22:48
 * @auther outman
 * @description
 **/
public class Main {
    public static void main(String[] args) {
        Company company = new Proxy();
        company.findWorker("Java");
    }
}
