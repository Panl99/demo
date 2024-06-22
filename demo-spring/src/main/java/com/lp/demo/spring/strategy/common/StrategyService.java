package com.lp.demo.spring.strategy.common;

import org.springframework.beans.factory.InitializingBean;

public abstract class StrategyService implements InitializingBean {

    public void invoke(String s) {
        throw new UnsupportedOperationException();
    }

}
