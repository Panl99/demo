package com.lp.demo.brcc.service.impl;

import com.lp.demo.brcc.service.Observer;
import com.lp.demo.brcc.service.Subject;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lp
 * @date 2021/7/3 9:33
 **/
@Service
public class Feed implements Subject {
    private final List<Observer> observers = new ArrayList<>();

    @Override
    public void registerObserver(Observer o) {
        this.observers.add(o);
    }

    @Override
    public void notifyObservers(Object obj) {
        observers.forEach(o -> o.notify(obj));
    }
}
