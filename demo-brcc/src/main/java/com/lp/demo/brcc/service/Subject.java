package com.lp.demo.brcc.service;

/**
 * @author lp
 * @date 2021/7/3 9:32
 **/
public interface Subject {
    void registerObserver(Observer o);
    void notifyObservers(Object obj);
}
