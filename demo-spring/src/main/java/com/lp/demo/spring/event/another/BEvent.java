package com.lp.demo.spring.event.another;


public class BEvent extends Event {

    private static final long serialVersionUID = 849335325658898895L;

    private String name;

    public BEvent(Object source) {
        super(source);
        this.name = this.getClass().getSimpleName();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "BEvent{" +
                "name='" + name + '\'' +
                "} " + super.toString();
    }
}
