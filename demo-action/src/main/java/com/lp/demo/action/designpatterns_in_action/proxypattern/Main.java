package com.lp.demo.action.designpatterns_in_action.proxypattern;

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
